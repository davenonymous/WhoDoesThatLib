package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;

import javax.annotation.Nullable;

/**
 * Interface for implementing method-level mod analyzers that process Java methods
 * during the scanning process.
 * <p>
 * This analyzer type is called for each method encountered during JAR scanning and
 * is ideal for examining method-level information such as:
 * <ul>
 *   <li>Method signatures and return types</li>
 *   <li>Method parameters and generic type information</li>
 *   <li>Method annotations and metadata</li>
 *   <li>Method access modifiers and attributes</li>
 *   <li>Exception declarations</li>
 *   <li>Method bodies and bytecode instructions</li>
 * </ul>
 * <p>
 * Implementations should focus on analyzing individual methods within classes.
 * For analyzing other elements:
 * <ul>
 *   <li>Use {@link IModClassAnalyzer} for class-level analysis</li>
 *   <li>Use {@link IModFieldAnalyzer} for field-level analysis</li>
 * </ul>
 * <p>
 * Method analyzers can collect structured data through the generic parameter T,
 * which must implement {@link IModResult}. This data is made available through
 * the methods inherited from {@link IModAnalyzerWithValue}.
 *
 * @param <T> The specific type of result this analyzer produces, must implement {@link IModResult}
 */
public interface IModMethodAnalyzer<T extends IModResult> extends IModAnalyzerWithValue<T> {

	/**
	 * Called for each method encountered during scanning.
	 * Implementations should analyze the method information and update their internal
	 * result state based on the findings.
	 *
	 * @param scanResult The current scan result being populated
	 * @param jarInfo Information about the JAR file containing this method
	 * @param modInfo Information about the mod containing this method, may be null if the method
	 *                does not belong to an identifiable mod
	 * @param classInfo Detailed information about the class containing this method
	 * @param methodInfo Detailed information about the method being analyzed
	 */
	void visitMethod(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, IClassInfo classInfo, IMethodInfo methodInfo);

	/**
	 * Called when the analysis of a new class begins, before any methods are visited.
	 * Use this method to prepare for processing all methods within a single class.
	 */
	default void onClassStart() {
	}

	/**
	 * Called when the analysis of a class is complete, after all methods have been visited.
	 * Use this method to finalize any class-level aggregation of method information.
	 */
	default void onClassEnd() {
	}
}
