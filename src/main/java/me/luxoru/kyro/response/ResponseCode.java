package me.luxoru.kyro.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * Represents an HTTP response status code with an associated message.
 * <p>
 * This class encapsulates HTTP status codes used in responses, including common status codes
 * for successful responses, client errors, and server errors. Each status code is associated with
 * a descriptive message.
 * </p>
 * <p>
 * The class provides predefined constants for standard HTTP response codes and methods to categorize
 * response codes into different categories (informational, successful, redirect, client error, and
 * server error).
 * </p>
 *
 * @author Luxoru
 */
@Getter
@AllArgsConstructor
public class ResponseCode {
    // Successful
    public static final ResponseCode OK = new ResponseCode(200, "Request was successful");
    public static final ResponseCode CREATED = new ResponseCode(201, "Request was successful and a new resource has been created");

    // Client Error
    public static final ResponseCode BAD_REQUEST = new ResponseCode(400, "The server could not understand the request due to invalid syntax");
    public static final ResponseCode UNAUTHORIZED = new ResponseCode(401, "The client must authenticate to get the requested response");
    public static final ResponseCode FORBIDDEN = new ResponseCode(403, "The client does not have access rights");
    public static final ResponseCode NOT_FOUND = new ResponseCode(404, "The server could not find the requested resource");
    public static final ResponseCode METHOD_NOT_ALLOWED = new ResponseCode(405, "The request method is known by the server but is not supported by the target resource");
    public static final ResponseCode GONE = new ResponseCode(410, "The requested content has been permanently deleted from the server");
    public static final ResponseCode IM_A_TEAPOT = new ResponseCode(418, "The server refuses the attempt to brew coffee with a teapot");
    public static final ResponseCode TOO_MANY_REQUESTS = new ResponseCode(429, "The client has sent too many requests");
    public static final ResponseCode UNAVAILABLE_FOR_LEGAL_REASONS = new ResponseCode(451, "The client has requested a resource that cannot legally be provided");

    // Server Error
    public static final ResponseCode INTERNAL_SERVER_ERROR = new ResponseCode(500, "The server has encountered a situation it doesn't know how to handle");
    public static final ResponseCode SERVICE_UNAVAILABLE = new ResponseCode(503, "The server is not ready to handle the request");
    public static final ResponseCode INSUFFICIENT_STORAGE = new ResponseCode(507, "The server is unable to store the representation needed to successfully complete the request");
    public static final ResponseCode LOOP_DETECTED = new ResponseCode(508, "The server detected an infinite loop whilst processing the request");

    private final int code;
    private final String message;

    /**
     * Checks if the response code is informational (100-199).
     * <p>
     * Informational responses provide additional information about the request and are typically used for
     * status updates or interim responses.
     * </p>
     *
     * @return {@code true} if the response code falls within the informational range (100-199),
     *         {@code false} otherwise
     */
    public boolean isInformational() {
        return code >= 100 && code <= 199;
    }

    /**
     * Checks if the response code indicates success (200-299).
     * <p>
     * Successful responses indicate that the request was successfully received, understood, and accepted.
     * This range includes responses such as OK (200) and Created (201).
     * </p>
     *
     * @return {@code true} if the response code falls within the successful range (200-299),
     *         {@code false} otherwise
     */
    public boolean isSuccessful() {
        return code >= 200 && code <= 299;
    }

    /**
     * Checks if the response code indicates a redirection (300-399).
     * <p>
     * Redirection responses indicate that further action is needed to fulfill the request, often redirecting
     * the client to a different resource. This range includes responses such as Moved Permanently (301) and
     * Found (302).
     * </p>
     *
     * @return {@code true} if the response code falls within the redirection range (300-399),
     *         {@code false} otherwise
     */
    public boolean isRedirect() {
        return code >= 300 && code <= 399;
    }

    /**
     * Checks if the response code indicates a client error (400-499).
     * <p>
     * Client error responses indicate that the request contains bad syntax or cannot be fulfilled. This range
     * includes errors such as Bad Request (400) and Unauthorized (401).
     * </p>
     *
     * @return {@code true} if the response code falls within the client error range (400-499),
     *         {@code false} otherwise
     */
    public boolean isClientError() {
        return code >= 400 && code <= 499;
    }

    /**
     * Checks if the response code indicates a server error (500-599).
     * <p>
     * Server error responses indicate that the server failed to fulfill a valid request. This range includes
     * errors such as Internal Server Error (500) and Service Unavailable (503).
     * </p>
     *
     * @return {@code true} if the response code falls within the server error range (500-599),
     *         {@code false} otherwise
     */
    public boolean isServerError() {
        return code >= 500 && code <= 599;
    }

}
