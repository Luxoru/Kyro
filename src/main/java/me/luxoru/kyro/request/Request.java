package me.luxoru.kyro.request;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.Getter;
import me.luxoru.kyro.util.HTTPUtils;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents an HTTP request in the context of the Kyro framework.
 * This class encapsulates details about the request, including headers,
 * parameters, and method information.
 *
 * @author Luxoru
 */
@Getter
public class Request {

    private final HttpExchange httpExchange;
    private final Headers headers;
    private final Map<String, String> parameters;
    private final RequestMethod method;

    /**
     * Constructs a new {@code Request} object.
     *
     * @param httpExchange the {@link HttpExchange} instance representing the HTTP request
     */
    public Request(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        this.headers = httpExchange.getRequestHeaders();
        this.parameters = Collections.unmodifiableMap(HTTPUtils.getParameters(httpExchange.getRequestURI().getQuery()));
        this.method = RequestMethod.fromName(httpExchange.getRequestMethod());
    }

    /**
     * Gets the URI of the request.
     *
     * @return the {@link URI} of the request
     */
    public URI getURI() {
        return httpExchange.getRequestURI();
    }

    /**
     * Gets the client address that made the request.
     *
     * @return the {@link InetSocketAddress} representing the client's address
     * @see InetSocketAddress for details on the address format
     */
    public InetSocketAddress getAddress() {
        return httpExchange.getRemoteAddress();
    }

    /**
     * Gets the number of headers provided in the request.
     *
     * @return the count of headers
     */
    public int headerCount() {
        int count = 0;
        for (List<String> list : this.headers.values()) {
            count += list.size();
        }
        return count;
    }

    /**
     * Gets the list of headers with the given name.
     *
     * @param name the name of the headers
     * @return a {@link List} of header values associated with the given name, or an empty list if none
     */
    public List<String> getHeaders(String name) {
        return headers.get(name);
    }

    /**
     * Gets the first header value with the given name.
     *
     * @param name the name of the header
     * @return the first header value, or {@code null} if no header with the given name is found
     */
    public String getHeader(String name) {
        return headers.getFirst(name);
    }

    /**
     * Gets the number of parameters provided in the request.
     *
     * @return the count of parameters
     */
    public int parameterCount() {
        return parameters.size();
    }

    /**
     * Gets the parameter value associated with the given name.
     *
     * @param name the name of the parameter
     * @return the parameter value, or {@code null} if no parameter with the given name is found
     */
    public String getParameter(String name) {
        return parameters.get(name);
    }

}
