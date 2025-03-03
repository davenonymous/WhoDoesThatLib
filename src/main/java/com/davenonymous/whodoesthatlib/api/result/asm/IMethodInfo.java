package com.davenonymous.whodoesthatlib.api.result.asm;

import org.objectweb.asm.Type;

import java.util.List;
import java.util.Optional;

public interface IMethodInfo {
	IClassInfo owner();

	String name();

	Type returnType();

	List<Type> parameters();

	List<IAnnotationInfo> annotations();

	IAccessBits access();

	default Optional<IAnnotationInfo> isAnnotatedWith(String annotationClassName) {
		return annotations().stream().filter(a -> a.type().getClassName().equals(annotationClassName)).findFirst();
	}

	default String locatableName() {
		return owner().getClassName() + "#" + name();
	}

	default String getReturnTypeName() {
		return returnType().getClassName();
	}

	default String getSimpleReturnTypeName() {
		return getReturnTypeName().substring(getReturnTypeName().lastIndexOf('.') + 1);
	}

	default String getSignature(boolean includeClassName) {
		StringBuilder fullName = new StringBuilder();
		String returnType = "void";
		if(returnType() != null) {
			var pos = returnType().getClassName().lastIndexOf('.');
			if(pos != -1) {
				returnType = returnType().getClassName().substring(pos + 1);
			} else {
				returnType = returnType().getClassName();
			}
		}
		fullName.append(returnType).append(" ");

		if(includeClassName) {
			fullName.append(owner().getClassName()).append("#");
		}

		fullName.append(name()).append("(");
		for(int i = 0; i < parameters().size(); i++) {
			var pos = parameters().get(i).getClassName().lastIndexOf('.');
			if(pos == -1) {
				fullName.append(parameters().get(i).getClassName());
			} else {
				fullName.append(parameters().get(i).getClassName().substring(pos + 1));
			}
			if(i < parameters().size() - 1) {
				fullName.append(", ");
			}
		}
		fullName.append(")");
		return fullName.toString();
	}
}
