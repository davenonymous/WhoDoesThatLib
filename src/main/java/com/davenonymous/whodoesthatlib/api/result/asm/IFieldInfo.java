package com.davenonymous.whodoesthatlib.api.result.asm;

import org.objectweb.asm.Type;

import java.util.Optional;
import java.util.Set;

public interface IFieldInfo {
	IClassInfo owner();

	Type type();

	String name();

	Set<IAnnotationInfo> annotations();

	IAccessBits access();

	default String getTypeName() {
		return type().getClassName();
	}

	default String getSimpleTypeName() {
		return getTypeName().substring(getTypeName().lastIndexOf('.') + 1);
	}

	default String locatableName() {
		return owner().getClassName() + "$" + getSimpleTypeName() + "#" + name();
	}

	default Optional<IAnnotationInfo> isAnnotatedWith(String annotationClassName) {
		return annotations().stream().filter(a -> a.type().getClassName().equals(annotationClassName)).findFirst();
	}
}
