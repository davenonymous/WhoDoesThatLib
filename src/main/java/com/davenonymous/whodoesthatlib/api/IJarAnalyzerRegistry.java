package com.davenonymous.whodoesthatlib.api;

import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;

import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * Registry for JAR file analyzers that process JAR contents during scanning.
 * <p>
 * This interface provides methods to register analyzers and run different
 * types of analysis on JAR files. Implementations should coordinate the
 * execution of all registered analyzers when scanning JAR files.
 */
public interface IJarAnalyzerRegistry {
	/**
	 * Registers a JAR analyzer with this registry.
	 *
	 * @param analyzer The analyzer to register
	 */
	void register(IJarAnalyzer analyzer);

	/**
	 * Runs only heritage analysis on the specified JAR file.
	 * Heritage analysis typically focuses on inheritance relationships,
	 * interfaces implemented, and class hierarchies.
	 *
	 * @param file The path to the JAR file
	 * @param fs The filesystem containing the JAR
	 * @return Information collected about the JAR file
	 */
	IJarInfo runHeritageOnly(Path file, FileSystem fs);

	/**
	 * Runs all registered analyzers on the specified JAR file.
	 * This performs a complete analysis of the JAR contents.
	 *
	 * @param file The path to the JAR file
	 * @param fs The filesystem containing the JAR
	 * @return Complete information collected about the JAR file
	 */
	IJarInfo runAll(Path file, FileSystem fs);
}
