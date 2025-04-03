package com.davenonymous.whodoesthatlib;

import com.davenonymous.whodoesthatlib.api.IJarAnalyzerRegistry;
import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.impl.DescriptorConfigVisitor;
import com.davenonymous.whodoesthatlib.impl.JarAnalyzers;
import com.davenonymous.whodoesthatlib.impl.JarFileVisitor;
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
	public Config config = new Config();
	public JarAnalyzers jarAnalyzers = new JarAnalyzers(this);

	public Config config() {
		return config;
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
		return this;
	}

	@Override
	public IScanResult process() throws IOException {
		var visitor = new JarFileVisitor(this);
		var heritageVisitor = new JarFileVisitor(this, true);
		for(var path : heritagePaths) {
			Files.walkFileTree(path, heritageVisitor);
		}
		for(var path : analysisPaths) {
			Files.walkFileTree(path, visitor);
		}

		var visitorJars = visitor.getJarFiles().values();
		var heritageJars = heritageVisitor.getJarFiles().values();

		var scanResult = new ScanResult(
			this,
			visitorJars,
			heritageJars
		);

		var analyzer = new ResultAnalyzer(this, scanResult);
		return scanResult;

	}
}
