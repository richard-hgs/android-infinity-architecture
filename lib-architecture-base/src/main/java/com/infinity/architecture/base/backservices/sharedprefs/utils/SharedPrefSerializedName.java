package com.infinity.architecture.base.backservices.sharedprefs.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates this member should be serialized to JSON with
 * the provided name value as its field name.
 *
 * <p>This annotation will override any {@link com.google.gson.FieldNamingPolicy}, including
 * the default field naming policy, that may have been set on the {@link com.google.gson.Gson}
 * instance.  A different naming policy can set using the {@code GsonBuilder} class.  See
 * {@link com.google.gson.GsonBuilder#setFieldNamingPolicy(com.google.gson.FieldNamingPolicy)}
 * for more information.</p>
 *
 * <p>Here is an example of how this annotation is meant to be used:</p>
 * <pre>
 * public class MyClass {
 *   &#64SerializedName("name") String a;
 *   &#64SerializedName(value="name1", alternate={"name2", "name3"}) String b;
 *   String c;
 *
 *   public MyClass(String a, String b, String c) {
 *     this.a = a;
 *     this.b = b;
 *     this.c = c;
 *   }
 * }
 * </pre>
 *
 * <p>The following shows the output that is generated when serializing an instance of the
 * above example class:</p>
 * <pre>
 * MyClass target = new MyClass("v1", "v2", "v3");
 * Gson gson = new Gson();
 * String json = gson.toJson(target);
 * System.out.println(json);
 *
 * ===== OUTPUT =====
 * {"name":"v1","name1":"v2","c":"v3"}
 * </pre>
 *
 * <p>NOTE: The value you specify in this annotation must be a valid JSON field name.</p>
 * While deserializing, all values specified in the annotation will be deserialized into the field.
 * For example:
 * <pre>
 *   MyClass target = gson.fromJson("{'name1':'v1'}", MyClass.class);
 *   assertEquals("v1", target.b);
 *   target = gson.fromJson("{'name2':'v2'}", MyClass.class);
 *   assertEquals("v2", target.b);
 *   target = gson.fromJson("{'name3':'v3'}", MyClass.class);
 *   assertEquals("v3", target.b);
 * </pre>
 * Note that MyClass.b is now deserialized from either name1, name2 or name3.
 *
 * @see com.google.gson.FieldNamingPolicy
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface SharedPrefSerializedName {
    /**
     * @return the desired name of the field when it is serialized or deserialized
     */
    String value();
}
