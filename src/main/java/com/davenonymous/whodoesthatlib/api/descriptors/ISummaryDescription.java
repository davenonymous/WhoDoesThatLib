package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Set;

/**
 * Interface representing a descriptor for code analysis results.
 * <p>
 * This interface defines methods to retrieve information about discovered elements
 * during code analysis. Each implementation represents a different type of code element
 * that can be identified, such as annotations, method calls, inheritance relationships, etc.
 * <p>
 * Implementations of this interface are typically configured from external configuration
 * entries and provide a unified way to identify and categorize code elements.
 */
public interface ISummaryDescription {
	/**
	 * Returns the unique configuration key associated with this description.
	 * @return The configuration key string
	 */
	String configKey();

	/**
	 * Returns the category of the result this description represents.
	 * Categories group related descriptions together.
	 * @return The category string
	 */
	String resultCategory();

	/**
	 * Returns the identifier of the specific result this description represents.
	 * @return The result identifier string
	 */
	String resultId();

	/**
	 * Returns a human-readable description of what this element represents.
	 * @return The description string
	 */
	String description();

	/**
	 * Returns the set of tags associated with this description.
	 * Tags can be used for filtering or organizing descriptions.
	 * @return A set of tag strings
	 */
	Set<String> tags();
}
