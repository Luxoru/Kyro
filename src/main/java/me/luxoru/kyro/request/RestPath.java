package me.luxoru.kyro.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify the path and HTTP method for a RESTful endpoint.
 * <p>
 * This annotation is applied to methods to define the route and the HTTP method
 * that should be used to access the endpoint. The {@code path} specifies the URL path
 * for the endpoint, and the {@code method} specifies the HTTP method (e.g., GET, POST).
 * </p>
 * <p>
 * This annotation is retained at runtime, allowing it to be used for runtime
 * reflection to map HTTP requests to the appropriate handler methods.
 * </p>
 *
 * @see RequestMethod
 * @author Luxoru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RestPath {

    /**
     * Specifies the path of the RESTful endpoint.
     *
     * @return the path as a string (e.g., "/api/resource")
     */
    String path();

    /**
     * Specifies the HTTP method to be used for the RESTful endpoint.
     *
     * @return the {@link RequestMethod} representing the HTTP method
     */
    RequestMethod method();

}
