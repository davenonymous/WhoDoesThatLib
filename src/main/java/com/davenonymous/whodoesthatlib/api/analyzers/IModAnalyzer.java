package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;

public interface IModAnalyzer {
	String getKey();

	default void onInit(IScanResult scanResult) {
	}

	default void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
	}

	default void onJarEnd(IScanResult scanResult, IJarInfo jarInfo) {
	}
}
