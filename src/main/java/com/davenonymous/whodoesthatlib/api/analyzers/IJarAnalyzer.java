package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface IJarAnalyzer {

	void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result);
}
