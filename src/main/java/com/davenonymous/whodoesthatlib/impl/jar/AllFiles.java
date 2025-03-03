package com.davenonymous.whodoesthatlib.impl.jar;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class AllFiles implements IJarAnalyzer {
	@Override
	public void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result) {
		if(file.toString().endsWith(".class")) {
			return;
		}

		if(result instanceof JarInfo jarInfo) {
			jarInfo.addFile(file);
		}
	}
}
