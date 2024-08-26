package me.luxoru.kyro.util;

import lombok.experimental.UtilityClass;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for handling HTTP-related operations.
 * <p>
 * This class provides static methods for processing HTTP data. It is marked as a utility class
 * with no instantiable instances.
 * </p>
 *
 *
 * @author Luxoru
 */
@UtilityClass
public class HTTPUtils {

    /**
     * Parses the query string from a URL and returns a map of parameter names to values.
     * <p>
     * The query string should be in the format "key1=value1&key2=value2". If the query string is null
     * or empty, an empty map is returned. Parameters without values are stored with an empty string as the value.
     * </p>
     *
     * @param query the query string to be parsed, or {@code null} or empty to return an empty map
     * @return a {@link Map} containing parameter names and their corresponding values
     */
    public static Map<String, String> getParameters(String query) {
        if (query == null || (query = query.trim()).isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> parameters = new HashMap<>();
        for (String param : query.split("&")) {
            String[] split = param.split("=");
            if (split.length > 1) {
                parameters.put(split[0], split[1]);
            } else {
                parameters.put(split[0], "");
            }
        }
        return parameters;
    }
}
