package com.davenonymous.whodoesthatlib.api.result.asm;

import org.objectweb.asm.Type;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a field found during class scanning.
 * Provides access to field metadata such as name, type, annotations,
 * and access modifiers.
 */
public interface IFieldInfo {
	/**
	 * Gets the class that owns this field.
	 *
	 * @return The class information of the owning class
	 */
	IClassInfo owner();

	/**
	 * Gets the ASM Type representing this field's type.
	 *
	 * @return The Type object for this field
	 */
	Type type();

	/**
	 * Gets the name of this field.
	 *
	 * @return The field name
	 */
	String name();

	/**
	 * Gets all annotations present on this field.
	 *
	 * @return Set of annotation information objects
	 */
	Set<IAnnotationInfo> annotations();

	/**
	 * Gets the access modifiers and flags for this field.
	 *
	 * @return Access flags information
	 */
	IAccessBits access();

	/**
	 * Gets the fully qualified type name of this field.
	 *
	 * @return The field's type name
	 */
	default String getTypeName() {
		return type().getClassName();
	}

	/**
	 * Gets the simple type name of this field (without package).
	 *
	 * @return The field's simple type name
	 */
	default String getSimpleTypeName() {
		return getTypeName().substring(getTypeName().lastIndexOf('.') + 1);
	}

	/**
	 * Gets a unique identifier for this field in the format "OwnerClass$FieldType#fieldName".
	 *
	 * @return A locatable identifier string for this field
	 */
	default String locatableName() {
		return owner().getClassName() + "$" + getSimpleTypeName() + "#" + name();
	}

	/**
	 * Checks if this field is annotated with a specific annotation.
	 *
	 * @param annotationClassName Fully qualified name of the annotation to check for
	 * @return Optional containing the annotation info if present, empty otherwise
	 */
	default Optional<IAnnotationInfo> isAnnotatedWith(String annotationClassName) {
		return annotations().stream().filter(a -> a.type().getClassName().equals(annotationClassName)).findFirst();
	}
}
