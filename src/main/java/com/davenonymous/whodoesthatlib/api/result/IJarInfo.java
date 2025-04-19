package com.davenonymous.whodoesthatlib.api.result;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.Manifest;

/**
 * Represents information about a JAR file.
 * This interface provides methods for accessing JAR contents, analyzing classes,
 * and working with JAR metadata.
 */
public interface IJarInfo {
	/**
	 * Gets the scanner that processed this JAR.
	 *
	 * @return The JAR scanner instance
	 */
	IJarScanner scanner();

	/**
	 * Gets the path to the JAR file.
	 *
	 * @return Path to the JAR file
	 */
	Path jar();

	/**
	 * Gets the SHA1 hash of the JAR file.
	 *
	 * @return SHA1 hash as string
	 */
	String getSHA1();

	/**
	 * Gets the parent JAR if this is a nested JAR.
	 *
	 * @return Parent JAR info or null if this is a top-level JAR
	 */
	IJarInfo parentJar();

	/**
	 * Gets the actual top-level JAR by traversing the parent chain.
	 *
	 * @return The root JAR containing this JAR
	 */
	IJarInfo actualJar();

	/**
	 * Gets the path to the parent JAR if this is a nested JAR.
	 *
	 * @return Optional path to the parent JAR
	 */
	Optional<Path> parentJarPath();

	/**
	 * Gets the content of a file within the JAR.
	 *
	 * @param path Path to the file within the JAR
	 * @return Optional byte array containing the file content
	 */
	Optional<byte[]> getFileContent(Path path);

	/**
	 * Provides access to the JAR's filesystem.
	 *
	 * @param fileSystemConsumer Consumer that operates on the filesystem
	 * @return true if the operation was successful, false otherwise
	 */
	boolean getFileSystem(Consumer<FileSystem> fileSystemConsumer);

	/**
	 * Gets the shortest common package name from all classes in the JAR.
	 *
	 * @return The shortest common package name
	 */
	String getShortestCommonPackage();

	/**
	 * Gets all classes discovered in the JAR.
	 *
	 * @return List of class information
	 */
	List<IClassInfo> getClasses();

	/**
	 * Gets all nested JARs found within this JAR.
	 *
	 * @return Set of nested JAR information
	 */
	Set<IJarInfo> getNestedJars();

	/**
	 * Gets the manifest of the JAR file.
	 *
	 * @return JAR manifest
	 */
	Manifest getManifest();

	/**
	 * Gets all files discovered in the JAR.
	 *
	 * @return List of file paths
	 */
	List<Path> files();

	/**
	 * Gets all tags associated with summary descriptions.
	 *
	 * @return Map of tags to summary descriptions
	 */
	Map<String, Set<ISummaryDescription>> tags();

	/**
	 * Gets analysis results of a specific type.
	 *
	 * @param resultClass The class of the analysis result
	 * @param <T> Type of the analysis result
	 * @return The analysis result
	 */
	<T extends IModResult> T getAnalysisResult(Class<T> resultClass);

	/**
	 * Gets the set of available analysis result IDs.
	 *
	 * @return Set of analysis result classes
	 */
	Set<Class<? extends IModResult>> getAnalysisIDs();

	/**
	 * Accesses a resource within the JAR.
	 *
	 * @param path Path to the resource
	 * @param consumer Consumer that operates on the resource input stream
	 * @return This JAR info instance for chaining
	 * @throws IOException If the resource cannot be accessed
	 */
	IJarInfo getResource(Path path, Consumer<InputStream> consumer) throws IOException;

	/**
	 * Gets classes annotated with a specific annotation.
	 *
	 * @param annotationClassName Fully qualified annotation class name
	 * @return Set of annotated classes
	 */
	Set<IClassInfo> getAnnotatedClasses(String annotationClassName);

	/**
	 * Gets methods annotated with a specific annotation.
	 *
	 * @param annotationClassName Fully qualified annotation class name
	 * @return Set of annotated methods
	 */
	Set<IMethodInfo> getAnnotatedMethods(String annotationClassName);

	/**
	 * Gets fields annotated with a specific annotation.
	 *
	 * @param annotationClassName Fully qualified annotation class name
	 * @return Set of annotated fields
	 */
	Set<IFieldInfo> getAnnotatedFields(String annotationClassName);

	/**
	 * Gets all types used by classes in the JAR.
	 *
	 * @return Set of used types
	 */
	Set<Type> getUsedTypes();

	/**
	 * Gets all method calls made by classes in the JAR.
	 *
	 * @return Set of called method signatures
	 */
	Set<String> getCalledMethods();

	/**
	 * Gets all localization keys found in the JAR.
	 *
	 * @return Set of localization keys
	 */
	Set<String> getLocalizations();

	/**
	 * Gets summary information in a format suitable for JSON serialization.
	 *
	 * @return Map of summary categories to sets of summary strings
	 */
	Map<String, Set<String>> getSummariesForJson();

	/**
	 * Gets all summaries with their associated extra data.
	 *
	 * @return Map of summary descriptions to lists of extra data objects
	 */
	Map<ISummaryDescription, List<Object>> getSummaries();


	/**
	 * Similar to {@link #getSummaries()} but includes the summaries from nested non-mod JARs.
	 *
	 * @return Map of summary descriptions to lists of extra data objects
	 */
	Map<ISummaryDescription, List<Object>> getNestedSummaries();

	/**
	 * Adds a summary to the JAR info.
	 *
	 * @param description The summary description
	 * @param extraData Additional data associated with the summary
	 */
	void addSummary(ISummaryDescription description, Object extraData);

	/**
	 * Adds a summary to the JAR info without extra data.
	 *
	 * @param description The summary description
	 */
	default void addSummary(ISummaryDescription description) {
		addSummary(description, null);
	}

	/**
	 * Gets a sorted list of all tags associated with this JAR.
	 *
	 * @return List of tag names
	 */
	default List<String> getTags() {
		return tags().keySet().stream().sorted(Comparator.naturalOrder()).toList();
	}
}
