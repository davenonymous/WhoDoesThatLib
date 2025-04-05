package com.davenonymous.whodoesthatlib.api.result.asm;

/**
 * Interface providing access to Java access modifiers and class attributes.
 * Used for querying the visibility, mutability, and nature of classes, methods, or fields
 * during bytecode analysis.
 */
public interface IAccessBits {
	/**
	 * Determines if the element has public visibility.
	 * @return true if the element is public, false otherwise
	 */
	boolean isPublic();

	/**
	 * Determines if the element has protected visibility.
	 * @return true if the element is protected, false otherwise
	 */
	boolean isProtected();

	/**
	 * Determines if the element has private visibility.
	 * @return true if the element is private, false otherwise
	 */
	boolean isPrivate();

	/**
	 * Determines if the element is marked as static.
	 * @return true if the element is static, false otherwise
	 */
	boolean isStatic();

	/**
	 * Determines if the element is marked as final.
	 * @return true if the element is final, false otherwise
	 */
	boolean isFinal();

	/**
	 * Determines if the element is marked as abstract.
	 * @return true if the element is abstract, false otherwise
	 */
	boolean isAbstract();

	/**
	 * Determines if the element is an interface.
	 * @return true if the element is an interface, false otherwise
	 */
	boolean isInterface();
}
