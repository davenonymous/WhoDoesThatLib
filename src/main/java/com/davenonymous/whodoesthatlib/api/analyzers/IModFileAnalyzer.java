package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;

import javax.annotation.Nullable;
import java.nio.file.Path;

/**
 * Interface for implementing file-level mod analyzers that process files
 * during the scanning process.
 * <p>
 * This analyzer type is called for each file encountered during JAR scanning and
 * is ideal for examining file-level information such as:
 * <ul>
 *   <li>Configuration files and their contents</li>
 *   <li>Resource files (textures, models, sounds, etc.)</li>
 *   <li>JSON data structures and schemas</li>
 *   <li>Custom file formats specific to the mod or game</li>
 *   <li>Metadata files like manifests or descriptors</li>
 * </ul>
 * <p>
 * Implementations should focus on analyzing non-class files within the mod JAR.
 * For analyzing Java code elements:
 * <ul>
 *   <li>Use {@link IModClassAnalyzer} for class-level analysis</li>
 *   <li>Use {@link IModFieldAnalyzer} for field-level analysis</li>
 *   <li>Use {@link IModMethodAnalyzer} for method-level analysis</li>
 * </ul>
 * <p>
 * Unlike other analyzers, this interface directly extends {@link IModAnalyzer} without
 * the value collection capability, as file analysis typically requires custom handling
 * of different file formats rather than a uniform result structure.
 */
public interface IModFileAnalyzer extends IModAnalyzer {
	/**
	 * Called for each file encountered during scanning.
	 * Implementations should analyze the file and update the scan result
	 * based on the findings.
	 *
	 * @param scanResult The current scan result being populated
	 * @param jarInfo Information about the JAR file containing this file
	 * @param modInfo Information about the mod containing this file, may be null if the file
	 *                does not belong to an identifiable mod
	 * @param file Path to the file being analyzed within the JAR
	 */
	void visitFile(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, Path file);
}
