package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;

import javax.annotation.Nullable;

/**
 * Interface for implementing field-level mod analyzers that process Java fields
 * during the scanning process.
 * <p>
 * This analyzer type is called for each field encountered during JAR scanning and
 * is ideal for examining field-level information such as:
 * <ul>
 *   <li>Field annotations and metadata</li>
 *   <li>Field types and generic parameters</li>
 *   <li>Field access modifiers (public, private, etc.)</li>
 *   <li>Static, final, transient, or volatile field attributes</li>
 *   <li>Initial values and constant fields</li>
 * </ul>
 * <p>
 * Implementations should focus on analyzing individual fields within classes.
 * For analyzing other elements:
 * <ul>
 *   <li>Use {@link IModClassAnalyzer} for class-level analysis</li>
 *   <li>Use {@link IModMethodAnalyzer} for method-level analysis</li>
 * </ul>
 * <p>
 * Field analyzers can collect structured data through the generic parameter T,
 * which must implement {@link IModResult}. This data is made available through
 * the methods inherited from {@link IModAnalyzerWithValue}.
 *
 * @param <T> The specific type of result this analyzer produces, must implement {@link IModResult}
 */
public interface IModFieldAnalyzer<T extends IModResult> extends IModAnalyzerWithValue<T> {

	/**
	 * Called for each field encountered during scanning.
	 * Implementations should analyze the field information and update their internal
	 * result state based on the findings.
	 *
	 * @param scanResult The current scan result being populated
	 * @param jarInfo Information about the JAR file containing this field
	 * @param modInfo Information about the mod containing this field, may be null if the field
	 *                does not belong to an identifiable mod
	 * @param classInfo Detailed information about the class containing this field
	 * @param fieldInfo Detailed information about the field being analyzed
	 */
	void visitField(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, IClassInfo classInfo, IFieldInfo fieldInfo);

	/**
	 * Called when the analysis of a new class begins, before any fields are visited.
	 * Use this method to prepare for processing all fields within a single class.
	 */
	default void onClassStart() {
	}

	/**
	 * Called when the analysis of a class is complete, after all fields have been visited.
	 * Use this method to finalize any class-level aggregation of field information.
	 */
	default void onClassEnd() {
	}
}
