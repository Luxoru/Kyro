package me.luxoru.kyro.request;

/**
 * Enum representing HTTP request methods.
 * <p>
 * This enum provides a representation of the commonly used HTTP methods: GET, POST, PUT, and DELETE.
 * It also provides a method to get an enum constant from its string representation.
 * </p>
 *
 * @author Luxoru
 */
public enum RequestMethod {


    GET, POST, PUT, DELETE;

    /**
     * Returns the {@code RequestMethod} corresponding to the given method name.
     * <p>
     * If the provided method name does not match any of the enum constants, this method returns {@code null}.
     * The comparison is case-sensitive.
     * </p>
     *
     * @param methodName the name of the HTTP method (e.g., "GET", "POST")
     * @return the corresponding {@code RequestMethod} enum constant, or {@code null} if no match is found
     */
    public static RequestMethod fromName(String methodName) {
        for (RequestMethod method : values()) {
            if (method.name().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

}
