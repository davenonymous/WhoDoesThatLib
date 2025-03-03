package com.davenonymous.whodoesthatlib.api;

import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public interface IJarAnalyzerRegistry {
	void register(IJarAnalyzer analyzer);

	IJarInfo runHeritageOnly(Path file, FileSystem fs);

	IJarInfo runAll(Path file, FileSystem fs);
}
