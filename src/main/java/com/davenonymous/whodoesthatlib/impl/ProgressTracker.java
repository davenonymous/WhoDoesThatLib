package com.davenonymous.whodoesthatlib.impl;

import com.davenonymous.whodoesthatlib.api.IProgressTracker;
import com.davenonymous.whodoesthatlib.api.IScanProgressListener;

public class ProgressTracker implements IProgressTracker {
	public int totalJars;
	public int scannedJars;
	public int analyzedJars;
	public int foundMods;

	public ProgressTracker reset() {
		this.totalJars = 0;
		this.scannedJars = 0;
		this.analyzedJars = 0;
		this.foundMods = 0;
		return this;
	}

	public int analyzedJars() {
		return analyzedJars;
	}

	public int foundMods() {
		return foundMods;
	}

	public int scannedJars() {
		return scannedJars;
	}

	public int totalJars() {
		return totalJars;
	}

	public float getProgress() {
		int requiredSteps = 2 * totalJars;
		if (requiredSteps <= 0) {
			return 0;
		}
		int doneSteps = scannedJars + analyzedJars;
		if (doneSteps <= 0) {
			return 0;
		}

		return (float) doneSteps / (float) requiredSteps;
	}

	public void callListener(IScanProgressListener listener, String event) {
		if (listener == null) {
			return;
		}

		listener.onProgress(this, event);
	}
}
