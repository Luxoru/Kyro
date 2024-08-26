package me.luxoru.kyro.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify the base path for a RESTful route at the class level.
 * <p>
 * This annotation is applied to classes to define the base URL path that should be
 * associated with all methods in the class. The {@code path} attribute specifies the
 * base path that will be prefixed to the paths defined by {@link RestPath} annotations
 * on methods within the class.
 * </p>
 * <p>
 * This annotation is retained at runtime, allowing it to be used for runtime reflection
 * to map HTTP requests to the appropriate handler classes.
 * </p>
 *
 * @see RestPath
 *
 * @author Luxoru
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Route {

    /**
     * Specifies the base path for the RESTful route.
     *
     * @return the base path as a string (e.g., "/api/resource")
     */
    String path() default "";

}
