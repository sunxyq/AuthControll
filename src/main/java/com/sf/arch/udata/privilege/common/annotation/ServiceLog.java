package com.sf.arch.udata.privilege.common.annotation;

import java.lang.annotation.*;

/**
 * Service log aspect annotation
 * @author Eddy Xiang
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface ServiceLog {
	String value() default "";
	int type() default 1;
}
