package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Interface for implementing JAR file analyzers that process individual files
 * within JAR archives during scanning.
 * <p>
 * Implementations should focus on examining file contents, structure, or metadata
 * to extract relevant information about the JAR file being analyzed.
 */
public interface IJarAnalyzer {
	/**
	 * Called for each file within a JAR archive during scanning.
	 * Implementations should process the file and update the result object
	 * with any relevant information discovered.
	 *
	 * @param scanner The scanner orchestrating the analysis
	 * @param jar The filesystem representing the JAR being analyzed
	 * @param file The path to the current file within the JAR
	 * @param attributes File attributes for the current file
	 * @param result The result object to update with discovered information
	 */
	void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result);
}
