package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;

import javax.annotation.Nullable;

/**
 * Interface for implementing class-level mod analyzers that process Java classes
 * during the scanning process.
 * <p>
 * This analyzer type is called for each class encountered during JAR scanning and
 * should be used for examining class-level information such as:
 * <ul>
 *   <li>Class annotations and metadata</li>
 *   <li>Class hierarchies and inheritance relationships</li>
 *   <li>Interface implementations</li>
 *   <li>Class access modifiers and attributes</li>
 * </ul>
 * <p>
 * Implementations should focus on analyzing class-level characteristics without
 * drilling down into fields or methods. For more granular analysis:
 * <ul>
 *   <li>Use {@link IModMethodAnalyzer} for method-level analysis</li>
 *   <li>Use {@link IModFieldAnalyzer} for field-level analysis</li>
 * </ul>
 * <p>
 * Class analyzers can collect structured data through the generic parameter T,
 * which must implement {@link IModResult}. This data is made available through
 * the methods inherited from {@link IModAnalyzerWithValue}.
 *
 * @param <T> The specific type of result this analyzer produces, must implement {@link IModResult}
 */
public interface IModClassAnalyzer<T extends IModResult> extends IModAnalyzerWithValue<T> {

	/**
	 * Called for each class encountered during scanning.
	 * Implementations should analyze the class information and update their internal
	 * result state based on the findings.
	 *
	 * @param scanResult The current scan result being populated
	 * @param jarInfo Information about the JAR file containing this class
	 * @param modInfo Information about the mod containing this class, may be null if the class
	 *                does not belong to an identifiable mod
	 * @param classInfo Detailed information about the class being analyzed
	 */
	void visitClass(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, IClassInfo classInfo);
}
