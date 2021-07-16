package org.springframework.samples.petclinic.owner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)                     // Annotation을 어디에 쓸지 (method)
@Retention(RetentionPolicy.RUNTIME)             // Annotation 정보를 언제까지 유지할 것인지 (runtime)
public @interface LogExecutionTime {

}
