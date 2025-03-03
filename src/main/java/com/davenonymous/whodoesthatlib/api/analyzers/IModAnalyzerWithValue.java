package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import com.google.gson.JsonElement;

public interface IModAnalyzerWithValue<T extends IModResult> extends IModAnalyzer {
	JsonElement encodedResult();

	T result();
}
