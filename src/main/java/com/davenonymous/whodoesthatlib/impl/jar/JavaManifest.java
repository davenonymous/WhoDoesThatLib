package com.davenonymous.whodoesthatlib.impl.jar;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.jar.Manifest;

public class JavaManifest implements IJarAnalyzer {
	@Override
	public void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result) {
		if(!file.toString().equals("/META-INF/MANIFEST.MF")) {
			return;
		}

		try(var is = Files.newInputStream(file)) {
			var manifest = new Manifest(is);
			if(result instanceof JarInfo jarInfo) {
				jarInfo.setManifest(manifest);
			}
		} catch (IOException ignore) {
		}
	}
}
