package com.davenonymous.whodoesthatlib.impl.result.asm;

import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.Map;

public record AnnotationInfo(
	ElementType annotationType,
	Type type,
	Map<String, Object> params
) implements IAnnotationInfo
{
	public static AnnotationInfo of(ElementType annotationType, Type type) {
		return new AnnotationInfo(annotationType, type, new HashMap<>());
	}

	public AnnotationInfo addParameter(String name, Object value) {
		this.params().put(name, value);
		return this;
	}

}
