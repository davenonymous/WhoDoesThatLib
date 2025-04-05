package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;

/**
 * Interface for implementing custom mod analyzers that process mod information
 * during the scanning process. Analyzers are called at specific points in the
 * scanning lifecycle to allow for custom processing of mod data.
 * <p>
 * Analyzer implementations can extract specific information, perform custom
 * analysis, or add additional metadata to scan results.
 * <p>
 * When implementing a mod analyzer, choose the appropriate specialized interface:
 * <ul>
 *   <li>{@link IModClassAnalyzer} - Use when you need to analyze entire classes,
 *       such as checking annotations, interfaces, or class structures.</li>
 *   <li>{@link IModMethodAnalyzer} - Use when you need to analyze methods,
 *       such as examining method signatures, parameters, or annotations.
 *       Provides additional lifecycle hooks for class processing.</li>
 *   <li>{@link IModFieldAnalyzer} - Use when you need to analyze fields,
 *       such as examining field types, annotations, or access modifiers.
 *       Provides additional lifecycle hooks for class processing.</li>
 *   <li>{@link IModFileAnalyzer} - Use when you need to analyze files,
 *       such as checking file contents, metadata, or structure.</li>*
 * </ul>
 * All specialized analyzers extend this base interface and add specific
 * functionality for their target element type.
 */
public interface IModAnalyzer {
	/**
	 * Returns a unique key that identifies this analyzer.
	 *
	 * @return A unique string identifier for this analyzer
	 */
	String getKey();

	/**
	 * Called once when the scanning process is initialized.
	 * Use this to set up any resources needed for the analysis.
	 *
	 * @param scanResult The scan result object being populated
	 */
	default void onInit(IScanResult scanResult) {
	}

	/**
	 * Called at the beginning of each JAR file analysis.
	 * Use this to prepare for processing a specific JAR file.
	 *
	 * @param scanResult The current scan result object
	 * @param jarInfo The information object for the JAR file being analyzed
	 */
	default void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
	}

	/**
	 * Called after a JAR file has been completely analyzed.
	 * Use this to finalize processing for a specific JAR file.
	 *
	 * @param scanResult The current scan result object
	 * @param jarInfo The information object for the JAR file that was analyzed
	 */
	default void onJarEnd(IScanResult scanResult, IJarInfo jarInfo) {
	}
}
