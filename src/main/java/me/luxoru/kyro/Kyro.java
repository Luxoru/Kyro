package me.luxoru.kyro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.luxoru.kyro.event.Cancellable;
import me.luxoru.kyro.event.KyroEvent;
import me.luxoru.kyro.request.Request;
import me.luxoru.kyro.request.RequestMethod;
import me.luxoru.kyro.request.RestPath;
import me.luxoru.kyro.request.Route;
import me.luxoru.kyro.response.Response;
import me.luxoru.kyro.response.ResponseCode;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Main class for the Kyro web server framework.
 * <p>
 * Kyro sets up and manages an HTTP server, handling routes and events according to the provided configuration.
 * </p>
 */
@Slf4j(topic = "Kyro")
public class Kyro {

    private static final Gson DEFAULT_GSON = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private final int port;
    private final HttpServer httpServer;
    private final Set<Object> routes;
    private final Set<KyroEvent> events;
    private final Gson gson;

    @Getter
    private boolean running = false;

    /**
     * Constructs a new Kyro instance with the specified configuration.
     *
     * @param port the port on which the server will listen
     * @param routes the set of route objects to handle requests
     * @param events the set of events to handle during request processing
     * @param gson the Gson instance for JSON serialization/deserialization
     */
    public Kyro(int port, Set<Object> routes, Set<KyroEvent> events, Gson gson) {
        this.port = port;
        this.routes = routes;
        this.events = events;
        this.gson = gson;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.setExecutor(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts the Kyro server. This method initializes routes and begins listening for incoming requests.
     *
     * @throws IllegalStateException if the server is already running
     */
    public void start() {
        if (isRunning()) {
            throw new IllegalStateException("Kyro already running");
        }

        httpServer.start();
        running = true;

        for (Object route : routes) {
            handleRoute(route);
        }

        log.info("Started Kyro on port {}", port);
    }

    /**
     * Registers and handles routes for the given class instance.
     *
     * @param classInstance the instance containing route methods
     * @throws IllegalStateException if the class is not annotated with {@link Route}
     */
    private void handleRoute(Object classInstance) {
        Class<?> clazz = classInstance.getClass();

        if (!clazz.isAnnotationPresent(Route.class)) {
            throw new IllegalStateException("Route %s is not annotated with @Route".formatted(clazz.getSimpleName()));
        }
        Route route = clazz.getAnnotation(Route.class);

        int methodsAdded = 0;

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(RestPath.class)) continue;

            RestPath restPath = method.getAnnotation(RestPath.class);
            String path = restPath.path();

            if (!route.path().isEmpty()) {
                path = route.path() + path;
            }

            httpServer.createContext(path, exchange -> {
                Request request = new Request(exchange);
                Response response = new Response();
                JsonObject jsonObject = new JsonObject();

                try {
                    boolean cancelled = false;

                    for (KyroEvent event : events) {
                        event.handle(request, response);

                        if (event instanceof Cancellable cancellableEvent) {
                            if (cancellableEvent.isCancelled()) {
                                cancelled = true;
                            }
                        }
                    }

                    if (cancelled) {
                        response.setResponseCode(ResponseCode.FORBIDDEN);
                        jsonObject.addProperty("success", false);
                        jsonObject.addProperty("error", "Request has been cancelled internally");
                        sendResponse(exchange, response, jsonObject);
                        return;
                    }

                    if (request.getMethod() == null) {
                        response.setResponseCode(ResponseCode.BAD_REQUEST);
                        jsonObject.addProperty("success", false);
                        jsonObject.addProperty("error", "Request method is null");
                        sendResponse(exchange, response, jsonObject);
                        return;
                    }

                    Object returned = method.invoke(classInstance, request, response);

                    if (method.getReturnType() != Void.class) {
                        jsonObject.addProperty("success", true);
                        jsonObject.add("value", gson.toJsonTree(returned));
                    } else if (restPath.method() == RequestMethod.GET) {
                        response.setResponseCode(ResponseCode.BAD_REQUEST);
                        sendResponse(exchange, response, jsonObject);
                        return;
                    }
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    log.error("Failed handling request:", ex);

                    if (response.getResponseCode() == ResponseCode.OK) {
                        response.setResponseCode(ResponseCode.INTERNAL_SERVER_ERROR);
                    }

                    jsonObject.addProperty("success", false);
                    jsonObject.addProperty("error", ex.getCause() != null ? ex.getCause().getLocalizedMessage() : ex.getMessage());
                }

                // Send the final response
                sendResponse(exchange, response, jsonObject);
            });

            methodsAdded++;
        }

        if (methodsAdded == 0) {
            log.warn("No routes found for route {}", clazz.getSimpleName());
        } else {
            log.info("Added {} routes to {}", methodsAdded, clazz.getSimpleName());
        }
    }

    /**
     * Sends the HTTP response back to the client.
     *
     * @param exchange the HTTP exchange containing the response
     * @param response the response object containing the response code
     * @param jsonObject the JSON object to be sent in the response body
     * @throws IOException if an I/O error occurs while sending the response
     */
    private void sendResponse(HttpExchange exchange, Response response, JsonObject jsonObject) throws IOException {
        String json = gson.toJson(jsonObject);
        log.info(json);
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(response.getResponseCode().getCode(), jsonBytes.length);
        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(jsonBytes);
        } catch (Exception e) {
            log.error("Error while writing response: ", e);
        }
    }

    /**
     * Stops the Kyro server and performs cleanup operations.
     *
     * @throws IllegalStateException if the server is not running
     */
    public void cleanup() {
        if (!running) {
            throw new IllegalStateException("Kyro is not running");
        }
        running = false;
        httpServer.stop(0);
    }

    /**
     * Builder class for constructing {@link Kyro} instances.
     * <p>
     * This builder allows for fluent configuration of the Kyro server.
     * </p>
     */
    public static class KyroBuilder {

        private final int port;
        private final Set<Object> routes;
        private final Set<KyroEvent> events;
        private Gson gson;

        /**
         * Constructs a new KyroBuilder with the specified port.
         *
         * @param port the port on which the server will listen
         */
        public KyroBuilder(int port) {
            this.port = port;
            this.routes = new HashSet<>();
            this.events = new HashSet<>();
            this.gson = DEFAULT_GSON;
        }

        /**
         * Adds a route object to the builder.
         *
         * @param route the route object to add
         * @return this builder instance
         */
        public KyroBuilder addRoute(Object route) {
            routes.add(route);
            return this;
        }

        /**
         * Adds an event to the builder.
         *
         * @param event the event to add
         * @return this builder instance
         */
        public KyroBuilder addEvent(KyroEvent event) {
            events.add(event);
            return this;
        }

        /**
         * Sets the Gson instance for JSON serialization/deserialization.
         *
         * @param gson the Gson instance to use
         * @return this builder instance
         */
        public KyroBuilder setGson(Gson gson) {
            this.gson = gson;
            return this;
        }

        /**
         * Builds and returns a new {@link Kyro} instance with the configured settings.
         *
         * @return a new Kyro instance
         */
        public Kyro build() {
            return new Kyro(port, routes, events, gson);
        }
    }
}
