package com.infinity.architecture.utils.backservices.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface GsonConfiguration {
    /**
     * Ignore this object in json writes nothing in json
     */
    boolean ignoreReadEntireObject() default false;

    /**
     * Ignore this object in json writes nothing in json
     */
    boolean ignoreWriteEntireObject() default false;

    /**
     * Require all non ignored fields
     * @return  true=Require all non ignored fields, can't be null
     */
    boolean requireReadAllFields() default true;

    /**
     * Ignore all fields except
     * @return  true=Ignore all non serialized fields, false=Send all fields
     */
    boolean ignoreWriteNonSerializedFields() default false;

    /**
     * Ignore all fields except
     * @return  true=Ignore all non serialized fields, false=Send all fields
     */
    boolean ignoreReadNonSerializedFields() default false;

    /**
     * Ignore all fields except
     * @return  true=Ignore all non serialized fields, false=Send all fields
     */
    boolean ignoreRWNonSerializedFields() default false;

    /**
     * Fields names/Serialized Names to be ignored in
     * @return Fields to be ignored
     */
    String[] ignoreWriteFields() default {};

    /**
     * Fields names/Serialized Names to be ignored in
     * @return Fields to be ignored
     */
    String[] ignoreReadFields() default {};

    /**
     * Fields names/Serialized Names to be ignored in
     * @return Fields to be ignored
     */
    String[] ignoreRWFields() default {};
}
