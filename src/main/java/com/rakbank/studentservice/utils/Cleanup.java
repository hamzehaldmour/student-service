package com.rakbank.studentservice.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to keep track / harvest the needed cleanups, Makes it easier to find cleanups using the IDE's functionality
 * Examples:
 *
 * @Cleanup("MDM")
 * @Cleanup("MDM", "Use new APis")
 * <p>
 * If its ugly. It's ok, its not meant to stay longer.
 * </p>
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Cleanup {
    String value(); // Component
    String comment() default "";
}
