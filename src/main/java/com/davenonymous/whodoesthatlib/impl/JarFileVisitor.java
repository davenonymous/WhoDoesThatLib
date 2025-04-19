package com.davenonymous.whodoesthatlib.impl;

import com.davenonymous.whodoesthatlib.JarScanner;
import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JarFileVisitor extends SimpleFileVisitor<Path> {
	private final Map<Path, IJarInfo> jarFiles = new HashMap<>();

	public Map<Path, IJarInfo> getJarFiles() {
		executor.shutdown();
		try {
			executor.awaitTermination(jarScanner.config().getScanTimeoutSeconds(), TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return jarFiles;
	}

	private boolean heritageOnly = false;
	private IJarInfo parentJar;
	private JarScanner jarScanner;
	private ExecutorService executor;

	public JarFileVisitor(JarScanner jarScanner) {
		this(jarScanner, null);
	}

	public JarFileVisitor(JarScanner jarScanner, IJarInfo parentJar) {
		this(jarScanner, parentJar, false);
	}

	public JarFileVisitor(JarScanner jarScanner, boolean heritageOnly) {
		this(jarScanner, null, heritageOnly);
	}

	public JarFileVisitor(JarScanner jarScanner, IJarInfo parentJar, boolean heritageOnly) {
		this.parentJar = parentJar;
		this.jarScanner = jarScanner;
		this.heritageOnly = heritageOnly;
		this.executor = Executors.newFixedThreadPool(jarScanner.config().getThreadsForScanning());
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		if(!file.toString().endsWith(".jar")) {
			return FileVisitResult.CONTINUE;
		}

		executor.execute(() -> {
			try(var fs = FileSystems.newFileSystem(file)) {
				IJarInfo jarResult;
				if(heritageOnly) {
					jarResult = jarScanner.jarAnalyzers().runHeritageOnly(file, fs);
				} else {
					jarResult = jarScanner.jarAnalyzers().runAll(file, fs);
				}
				if(parentJar != null && jarResult instanceof JarInfo jarInfo && parentJar instanceof JarInfo parentJarInfo) {
					jarInfo.setParentJar(parentJar);
					parentJarInfo.addChildJar(jarInfo);
				}
				jarFiles.put(file, jarResult);
				jarScanner.progressTracker.scannedJars++;
				if(parentJar != null) {
					jarScanner.progressTracker.totalJars++;
				}
				jarScanner.reportProgress("Scanned " + file.getFileName().toString());

				// Search for nested jars
				Path rootInJar = fs.getPath("/");
				var nestedJarVisitor = new JarFileVisitor(jarScanner, jarResult, heritageOnly);
				Files.walkFileTree(rootInJar, nestedJarVisitor);
				jarFiles.putAll(nestedJarVisitor.getJarFiles());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

		return FileVisitResult.CONTINUE;
	}
}
