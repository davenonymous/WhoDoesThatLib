package com.davenonymous.whodoesthatlib;

import com.davenonymous.whodoesthatlib.api.IJarAnalyzerRegistry;
import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.IScanProgressListener;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.impl.DescriptorConfigVisitor;
import com.davenonymous.whodoesthatlib.impl.JarAnalyzers;
import com.davenonymous.whodoesthatlib.impl.JarFileVisitor;
import com.davenonymous.whodoesthatlib.impl.ProgressTracker;
import com.davenonymous.whodoesthatlib.impl.result.ScanResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

public class JarScanner implements IJarScanner {
	private List<Path> analysisPaths = new ArrayList<>();
	private List<Path> heritagePaths = new ArrayList<>();
	private List<Path> descriptorConfigPaths = new ArrayList<>();

	public Map<String, BiFunction<String, Map<String, Object>, ISummaryDescription>> descriptorTypeLoaders = new HashMap<>();
	public Map<String, List<ISummaryDescription>> descriptors;
	public Map<String, List<ISummaryDescription>> descriptorsByTag;
	public Config config = new Config();
	public JarAnalyzers jarAnalyzers = new JarAnalyzers(this);
	public List<IScanProgressListener> listeners = new ArrayList<>();
	public ProgressTracker progressTracker = new ProgressTracker();

	public Config config() {
		return config;
	}

	public List<ISummaryDescription> getSummaryDescriptionsByTag(String tag) {
		return descriptorsByTag.getOrDefault(tag, Collections.emptyList());
	}

	public IJarAnalyzerRegistry jarAnalyzers() {
		return jarAnalyzers;
	}

	public JarScanner addAnalysisPath(Path path) {
		analysisPaths.add(path);
		return this;
	}

	public JarScanner addAnalysisPath(Collection<Path> paths) {
		analysisPaths.addAll(paths);
		return this;
	}

	public JarScanner addAnalysisPath(Path... paths) {
		Collections.addAll(analysisPaths, paths);
		return this;
	}

	public JarScanner addHeritagePath(Path path) {
		heritagePaths.add(path);
		return this;
	}

	public JarScanner addHeritagePath(Collection<Path> paths) {
		heritagePaths.addAll(paths);
		return this;
	}

	public JarScanner addHeritagePath(Path... paths) {
		Collections.addAll(heritagePaths, paths);
		return this;
	}

	public JarScanner addDescriptorConfigPath(Path path) {
		descriptorConfigPaths.add(path);
		return this;
	}

	public JarScanner addDescriptorConfigPath(Collection<Path> paths) {
		descriptorConfigPaths.addAll(paths);
		return this;
	}

	public JarScanner addDescriptorConfigPath(Path... paths) {
		Collections.addAll(descriptorConfigPaths, paths);
		return this;
	}

	@Override
	public IJarScanner registerDescriptorType(String configKey, BiFunction<String, Map<String, Object>, ISummaryDescription> descriptorFactory) {
		this.descriptorTypeLoaders.put(configKey, descriptorFactory);
		return this;
	}

	public IJarScanner loadDescriptors() throws IOException {
		var descriptorVisitor = new DescriptorConfigVisitor(this.descriptorTypeLoaders);
		for(var path : descriptorConfigPaths) {
			Files.walkFileTree(path, descriptorVisitor);
		}
		this.descriptors = descriptorVisitor.result();
		this.descriptorsByTag = new HashMap<>();
		this.descriptors.values().stream().flatMap(Collection::stream).forEach(description -> {
			for(var tag : description.tags()) {
				this.descriptorsByTag.computeIfAbsent(tag, k -> new ArrayList<>()).add(description);
			}
		});

		return this;
	}

	@Override
	public IJarScanner addProgressListener(IScanProgressListener listener) {
		this.listeners.add(listener);
		return this;
	}

	public IJarScanner reportProgress(String message) {
		for(var listener : listeners) {
			progressTracker.callListener(listener, message);
		}
		return this;
	}

	@Override
	public IScanResult process() throws IOException {
		progressTracker.reset();
		reportProgress("Starting scan");
		for(var path : heritagePaths) {
			if(Files.isDirectory(path)) {
				try(var stream = Files.list(path)) {
					progressTracker.totalJars += (int) stream.filter(p -> p.toString().endsWith(".jar")).count();
				}
			} else if (path.toString().endsWith(".jar")) {
				progressTracker.totalJars++;
			}
			reportProgress("Searching heritage paths for jars");
		}

		for(var path : analysisPaths) {
			if(Files.isDirectory(path)) {
				try(var stream = Files.list(path)) {
					progressTracker.totalJars += (int) stream.filter(p -> p.toString().endsWith(".jar")).count();
				}
			} else if (path.toString().endsWith(".jar")) {
				progressTracker.totalJars++;
			}
			reportProgress("Searching analysis paths for jars");
		}

		var visitor = new JarFileVisitor(this);
		var heritageVisitor = new JarFileVisitor(this, true);
		for(var path : heritagePaths) {
			Files.walkFileTree(path, heritageVisitor);
			reportProgress("Scanning jars in heritage path");
		}
		for(var path : analysisPaths) {
			Files.walkFileTree(path, visitor);
			reportProgress("Scanning jars in analysis path");
		}

		var visitorJars = visitor.getJarFiles().values();
		var heritageJars = heritageVisitor.getJarFiles().values();

		reportProgress("Building heritage tree");
		var scanResult = new ScanResult(
			this,
			visitorJars,
			heritageJars
		);

		reportProgress("Analyzing jars");
		var analyzer = new ResultAnalyzer(this, scanResult);
		progressTracker.analyzedJars = progressTracker.scannedJars;
		reportProgress("Done");
		return scanResult;

	}
}
