package com.davenonymous.whodoesthatlib.api.result.asm;

import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import org.objectweb.asm.Type;

import java.util.Optional;
import java.util.Set;

public interface IClassInfo {

	Type type();

	Type inherits();

	Set<Type> interfaces();

	Set<IMethodInfo> methods();

	Set<IAnnotationInfo> annotations();

	Set<IFieldInfo> fields();

	Set<Type> usedTypes();

	Set<String> calledMethods();

	default Optional<IAnnotationInfo> isAnnotatedWith(String annotationClassName) {
		return annotations().stream().filter(a -> a.type().getClassName().equals(annotationClassName)).findFirst();
	}

	Set<String> calledMethods(String methodName);

	Set<Type> usesType(String typeQuery);

	boolean inherits(IScanResult scanResult, String parentClassName);

	IAccessBits access();

	default String getClassName() {
		return type().getClassName();
	}

	default String getSimpleName() {
		String className = type().getClassName();
		int lastDot = className.lastIndexOf('.');
		if(lastDot <= 0) {
			return className;
		}
		return className.substring(lastDot + 1);
	}

	default String getPackageName() {
		String className = type().getClassName();
		int lastDot = className.lastIndexOf('.');
		if(lastDot <= 0) {
			return className;
		}
		return className.substring(0, lastDot);
	}
}
