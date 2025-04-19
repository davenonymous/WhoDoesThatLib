package com.davenonymous.whodoesthatlib.api;

public interface IProgressTracker {
	int analyzedJars();

	int foundMods();

	int scannedJars();

	int totalJars();

	float getProgress();
}
