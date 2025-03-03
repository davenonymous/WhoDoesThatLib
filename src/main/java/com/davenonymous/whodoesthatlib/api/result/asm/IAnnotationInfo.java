package com.davenonymous.whodoesthatlib.api.result.asm;

import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.util.Map;

public interface IAnnotationInfo {
	ElementType annotationType();

	Type type();

	Map<String, Object> params();

	default String getClassName() {
		return type().getClassName();
	}

	default String getSimpleName() {
		return type().getClassName().substring(type().getClassName().lastIndexOf('.') + 1);
	}
}
