package com.davenonymous.whodoesthatlib.api.result;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents the result of a scan performed by the {@link IJarScanner} interface.<br/>
 * This interface provides methods to query information about classes, their inheritance relationships, and the jars processed during the scan.<br/>
 * <p>
 * It also provides some convenience methods to retrieve information about classes and their relationships.<br/>
 *
 * @see IJarScanner
 */
public interface IScanResult {
	/**
	 * Returns whether the class with the given name inherits from the class with the given parent name.<br/>
	 * This method checks the inheritance hierarchy of the class and its superclasses.<br/>
	 * <p>
	 * If you are missing an inheritance result (e.g. when the query for {@code LivingEntity} does not include {@code Animal}s),
	 * please make sure to have loaded the correct inheritance jars when creating this scan result.
	 *
	 * @param className       The full name of the class to check.
	 * @param parentClassName The full name of the parent class to check against.
	 * @return true if the class inherits from the parent class, false otherwise.
	 */
	boolean doesInheritFrom(String className, String parentClassName);


	/**
	 * Returns a set of all superclass and interface names that the given class inherits from or implements.<br/>
	 * This includes both direct and indirect relationships in the inheritance hierarchy.
	 *
	 * @param className The full name of the class to get supertypes for
	 * @return A set of fully qualified class names representing all supertypes
	 */
	Set<String> supersOf(String className);

	/**
	 * Returns all classes that extend or implement the specified parent class.<br/>
	 * This includes both direct and indirect subclasses/implementations.
	 *
	 * @param parentClassName The fully qualified name of the parent class/interface to search for
	 * @return A set of {@link IClassInfo} objects representing all classes that inherit from the specified parent
	 */
	Set<IClassInfo> classesWithType(String parentClassName);

	/**
	 * Convenience method that works like {@link #classesWithType(String)} but accepts a Class object instead of a class name.
	 *
	 * @param parentClass The parent class to search for
	 * @return A set of {@link IClassInfo} objects representing all classes that inherit from the specified parent
	 */
	default Set<IClassInfo> classesWithType(Class<?> parentClass) {
		return classesWithType(parentClass.getName());
	}

	/**
	 * Serializes the scan result to a JSON element.<br/><br/>
	 * <p>
	 * The complexity of the generated JSON depends on the settings made in the {@link com.davenonymous.whodoesthatlib.Config}
	 *
	 * @return An Optional containing the JSON representation of this scan result, or empty if serialization failed
	 */
	Optional<JsonElement> asJson();

	/**
	 * Returns all jar information objects that were processed during the scan.
	 *
	 * @return A list of {@link IJarInfo} objects containing information about each processed jar
	 */
	List<IJarInfo> jars();

	/**
	 * Returns all descriptor configurations that were used during scanning.
	 *
	 * @return A map of descriptor category names to lists of their corresponding {@link ISummaryDescription} objects
	 */
	Map<String, List<ISummaryDescription>> getDescriptors();

	/**
	 * Returns all descriptors of a specific category that were used during scanning.
	 *
	 * @param category The category name of the descriptors to retrieve
	 * @param <T>      The type of the summary description
	 * @return A list of descriptors of the specified category
	 */
	<T extends ISummaryDescription> List<T> getSummaryDescriptions(String category);

	/**
	 * Returns a list of all descriptors matching the given tag.<br/>
	 *
	 * @return A list of {@link ISummaryDescription}s.
	 */
	List<ISummaryDescription> getSummaryDescriptionsByTag(String tag);
}
