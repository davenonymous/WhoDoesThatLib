package com.davenonymous.whodoesthatlib.impl;

import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class DescriptorConfigVisitor extends SimpleFileVisitor<Path> {
	private Yaml yaml;
	Map<String, BiFunction<String, Map<String, Object>, ISummaryDescription>> descriptorLoaders;
	Map<String, List<ISummaryDescription>> result;

	public DescriptorConfigVisitor(Map<String, BiFunction<String, Map<String, Object>, ISummaryDescription>> descriptorLoaders) {
		this.descriptorLoaders = descriptorLoaders;
		this.result = new HashMap<>();

		var loaderOptions = new LoaderOptions();
		yaml = new Yaml(loaderOptions);

	}

	public Map<String, List<ISummaryDescription>> result() {
		return result;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
		if(!file.toString().endsWith(".yml") && !file.toString().endsWith(".yaml")) {
			return FileVisitResult.CONTINUE;
		}

		var content = Files.readString(file);
		Map<String, List<Map<String, Object>>> config = yaml.load(content);
		for(String descriptorName : config.keySet()) {
			if(!descriptorLoaders.containsKey(descriptorName)) {
				continue;
			}

			List<Map<String, Object>> descriptorConfigs = config.get(descriptorName);
			for(Map<String, Object> descriptorConfig : descriptorConfigs) {
				var descriptorFactory = descriptorLoaders.get(descriptorName);
				ISummaryDescription descriptorResult = descriptorFactory.apply(descriptorName, descriptorConfig);
				this.result.computeIfAbsent(descriptorName, k -> new ArrayList<>()).add(descriptorResult);
			}
		}

		return FileVisitResult.CONTINUE;
	}
}
