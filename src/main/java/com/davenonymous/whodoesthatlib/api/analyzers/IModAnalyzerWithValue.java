package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import com.google.gson.JsonElement;

/**
 * Extension of the {@link IModAnalyzer} interface that adds support for
 * retrieving analysis results both as a typed object and as a JSON element.
 * <p>
 * This interface is typically used by analyzers that need to collect and
 * return structured data about mods. The results can be used for reporting,
 * visualization, or further processing.
 *
 * @param <T> The specific type of result this analyzer produces, must implement {@link IModResult}
 */
public interface IModAnalyzerWithValue<T extends IModResult> extends IModAnalyzer {
	/**
	 * Returns the analysis results encoded as a JSON element.
	 * This is useful for serialization or when the results need to be
	 * transmitted or stored in a format-agnostic way.
	 *
	 * @return A JsonElement representing the analysis results
	 */
	JsonElement encodedResult();

	/**
	 * Returns the analyzed data as a strongly-typed object.
	 * This provides direct programmatic access to the analysis results.
	 *
	 * @return The analysis results as the specified type T
	 */
	T result();
}
