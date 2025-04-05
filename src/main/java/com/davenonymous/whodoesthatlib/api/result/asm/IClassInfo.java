package com.davenonymous.whodoesthatlib.api.result.asm;

import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import org.objectweb.asm.Type;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a class found during scanning operations.
 * Provides access to class metadata such as type information, inheritance structure,
 * methods, fields, annotations, and usage information.
 */
public interface IClassInfo {

	/**
	 * Gets the ASM Type representing this class.
	 *
	 * @return The Type object for this class
	 */
	Type type();

	/**
	 * Gets the ASM Type representing this class's parent class.
	 *
	 * @return The Type object for the parent class
	 */
	Type inherits();

	/**
	 * Gets the set of interfaces implemented by this class.
	 *
	 * @return Set of Type objects representing implemented interfaces
	 */
	Set<Type> interfaces();

	/**
	 * Gets all methods defined in this class.
	 *
	 * @return Set of method information objects
	 */
	Set<IMethodInfo> methods();

	/**
	 * Gets all annotations present on this class.
	 *
	 * @return Set of annotation information objects
	 */
	Set<IAnnotationInfo> annotations();

	/**
	 * Gets all fields defined in this class.
	 *
	 * @return Set of field information objects
	 */
	Set<IFieldInfo> fields();

	/**
	 * Gets all types referenced by this class.
	 *
	 * @return Set of Type objects representing used types
	 */
	Set<Type> usedTypes();

	/**
	 * Gets all method names that are called within this class.
	 *
	 * @return Set of method names called by this class
	 */
	Set<String> calledMethods();

	/**
	 * Checks if this class is annotated with a specific annotation.
	 *
	 * @param annotationClassName Fully qualified name of the annotation to check for
	 * @return Optional containing the annotation info if present, empty otherwise
	 */
	default Optional<IAnnotationInfo> isAnnotatedWith(String annotationClassName) {
		return annotations().stream().filter(a -> a.type().getClassName().equals(annotationClassName)).findFirst();
	}

	/**
	 * Gets all calls to methods with the specified name.
	 *
	 * @param methodName Name of the method to find calls for
	 * @return Set of method calls matching the name
	 */
	Set<String> calledMethods(String methodName);

	/**
	 * Gets all types used by this class that match the given query.
	 *
	 * @param typeQuery Query string to match against types
	 * @return Set of matching Type objects
	 */
	Set<Type> usesType(String typeQuery);

	/**
	 * Checks if this class inherits from the specified parent class.
	 *
	 * @param scanResult Scan result containing class hierarchy information
	 * @param parentClassName Fully qualified name of the potential parent class
	 * @return True if this class inherits from the specified class, false otherwise
	 */
	boolean inherits(IScanResult scanResult, String parentClassName);

	/**
	 * Gets the access modifiers and flags for this class.
	 *
	 * @return Access flags information
	 */
	IAccessBits access();

	/**
	 * Gets the fully qualified class name.
	 *
	 * @return The fully qualified name of this class
	 */
	default String getClassName() {
		return type().getClassName();
	}

	/**
	 * Gets the simple name of this class (without package).
	 *
	 * @return The simple class name
	 */
	default String getSimpleName() {
		String className = type().getClassName();
		int lastDot = className.lastIndexOf('.');
		if(lastDot <= 0) {
			return className;
		}
		return className.substring(lastDot + 1);
	}

	/**
	 * Gets the package name of this class.
	 *
	 * @return The package name
	 */
	default String getPackageName() {
		String className = type().getClassName();
		int lastDot = className.lastIndexOf('.');
		if(lastDot <= 0) {
			return className;
		}
		return className.substring(0, lastDot);
	}
}
