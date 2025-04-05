package com.davenonymous.whodoesthatlib.api.result.asm;

import org.objectweb.asm.Type;

import java.util.List;
import java.util.Optional;

/**
 * Represents a method found during class scanning.
 * Provides access to method metadata such as name, return type, parameters,
 * annotations, and access modifiers.
 */
public interface IMethodInfo {
	/**
	 * Gets the class that owns this method.
	 *
	 * @return The class information of the owning class
	 */
	IClassInfo owner();

	/**
	 * Gets the name of this method.
	 *
	 * @return The method name
	 */
	String name();

	/**
	 * Gets the ASM Type representing this method's return type.
	 *
	 * @return The Type object for the return type
	 */
	Type returnType();

	/**
	 * Gets the list of parameter types for this method.
	 *
	 * @return List of Type objects representing parameter types
	 */
	List<Type> parameters();

	/**
	 * Gets all annotations present on this method.
	 *
	 * @return List of annotation information objects
	 */
	List<IAnnotationInfo> annotations();

	/**
	 * Gets the access modifiers and flags for this method.
	 *
	 * @return Access flags information
	 */
	IAccessBits access();

	/**
	 * Checks if this method is annotated with a specific annotation.
	 *
	 * @param annotationClassName Fully qualified name of the annotation to check for
	 * @return Optional containing the annotation info if present, empty otherwise
	 */
	default Optional<IAnnotationInfo> isAnnotatedWith(String annotationClassName) {
		return annotations().stream().filter(a -> a.type().getClassName().equals(annotationClassName)).findFirst();
	}

	/**
	 * Gets a unique identifier for this method in the format "OwnerClass#methodName".
	 *
	 * @return A locatable identifier string for this method
	 */
	default String locatableName() {
		return owner().getClassName() + "#" + name();
	}

	/**
	 * Gets the fully qualified return type name of this method.
	 *
	 * @return The method's return type name
	 */
	default String getReturnTypeName() {
		return returnType().getClassName();
	}

	/**
	 * Gets the simple return type name of this method (without package).
	 *
	 * @return The method's simple return type name
	 */
	default String getSimpleReturnTypeName() {
		return getReturnTypeName().substring(getReturnTypeName().lastIndexOf('.') + 1);
	}

	/**
	 * Gets the method signature in a human-readable format.
	 *
	 * @param includeClassName Whether to include the class name in the signature
	 * @return A formatted method signature string
	 */
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
