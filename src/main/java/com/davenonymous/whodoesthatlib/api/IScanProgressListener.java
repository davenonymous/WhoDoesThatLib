package com.davenonymous.whodoesthatlib.api;

@FunctionalInterface
public interface IScanProgressListener {
	void onProgress(IProgressTracker progress, String event);
}
