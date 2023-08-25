package org.tenggara.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) // The Annotation is reserved only in the source code
@Target({ElementType.TYPE, ElementType.FIELD})// Used to modify the class
public @interface Getter {

}
