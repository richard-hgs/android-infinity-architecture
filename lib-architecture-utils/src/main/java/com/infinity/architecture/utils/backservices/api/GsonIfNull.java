package com.infinity.architecture.utils.backservices.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GsonIfNull {
    /**
     * If value is null don't put key in json if true
     */
    boolean ifNullDontWriteKeyInJson() default false;
}
