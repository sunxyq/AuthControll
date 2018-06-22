package com.sf.arch.udata.privilege.common.annotation;

import java.lang.annotation.*;

/**
 * log annotation, insert logs into database
 * @author Eddy Xiang
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface ControllerLog {
	String value() default "";
	int type() default 0;
}
