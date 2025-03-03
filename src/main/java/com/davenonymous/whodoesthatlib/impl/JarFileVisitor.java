package com.davenonymous.whodoesthatlib.impl;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class JarFileVisitor extends SimpleFileVisitor<Path> {
	private Map<Path, IJarInfo> jarFiles = new HashMap<>();

	public Map<Path, IJarInfo> getJarFiles() {
		return jarFiles;
	}

	private boolean heritageOnly = false;
	private IJarInfo parentJar;
	private IJarScanner jarScanner;

	public JarFileVisitor(IJarScanner jarScanner) {
		this.parentJar = null;
		this.jarScanner = jarScanner;
	}

	public JarFileVisitor(IJarScanner jarScanner, IJarInfo parentJar) {
		this.parentJar = parentJar;
		this.jarScanner = jarScanner;
	}

	public JarFileVisitor(IJarScanner jarScanner, boolean heritageOnly) {
		this(jarScanner);
		this.heritageOnly = heritageOnly;
	}

	public JarFileVisitor(IJarScanner jarScanner, IJarInfo parentJar, boolean heritageOnly) {
		this(jarScanner, parentJar);
		this.heritageOnly = heritageOnly;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		if(!file.toString().endsWith(".jar")) {
			return FileVisitResult.CONTINUE;
		}

		try(var fs = FileSystems.newFileSystem(file)) {
			IJarInfo jarResult;
			if(heritageOnly) {
				jarResult = jarScanner.jarAnalyzers().runHeritageOnly(file, fs);
			} else {
				jarResult = jarScanner.jarAnalyzers().runAll(file, fs);
			}
			if(parentJar != null && jarResult instanceof JarInfo jarInfo) {
				jarInfo.setParentJar(parentJar);
			}
			jarFiles.put(file, jarResult);

			// Search for nested jars
			Path rootInJar = fs.getPath("/");
			var nestedJarVisitor = new JarFileVisitor(jarScanner, jarResult, heritageOnly);
			Files.walkFileTree(rootInJar, nestedJarVisitor);
			jarFiles.putAll(nestedJarVisitor.getJarFiles());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return FileVisitResult.CONTINUE;
	}
}
