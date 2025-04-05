package com.davenonymous.whodoesthatlib.api.result.asm;

import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.util.Map;

/**
 * Represents an annotation found during class scanning.
 * This interface provides access to annotation metadata such as its type,
 * location (element type), and parameter values.
 */
public interface IAnnotationInfo {
	/**
	 * Gets the element type this annotation is attached to (e.g., TYPE, METHOD, FIELD).
	 *
	 * @return The element type where this annotation is used
	 */
	ElementType annotationType();

	/**
	 * Gets the ASM Type representing this annotation's class.
	 *
	 * @return The Type object for this annotation
	 */
	Type type();

	/**
	 * Gets the annotation's parameters as key-value pairs.
	 *
	 * @return Map of parameter names to their values
	 */
	Map<String, Object> params();

	/**
	 * Gets the fully qualified class name of this annotation.
	 *
	 * @return The annotation's fully qualified class name
	 */
	default String getClassName() {
		return type().getClassName();
	}

	/**
	 * Gets the simple name of this annotation (without package).
	 *
	 * @return The annotation's simple name
	 */
	default String getSimpleName() {
		return type().getClassName().substring(type().getClassName().lastIndexOf('.') + 1);
	}
}
