package com.davenonymous.whodoesthatlib;

import com.davenonymous.whodoesthatlib.api.IConfig;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Config implements IConfig {

	private Set<IncludedOutput> includedOutputs = new HashSet<>();
	private Set<String> modBlacklist = new HashSet<>();
	private Set<String> dependencyBlacklist = new HashSet<>();
	private Set<String> languagesToInclude = new HashSet<>();

	private boolean useDescriptionsInSummary = false;
	private boolean enableCodeParsing = true;
	private int threadsForScanning = 2;
	private int scanTimeoutSeconds = 300;

	public Config() {
		this.addLanguageToInclude("en_us");
	}

	public boolean doesInclude(IncludedOutput output) {
		return includedOutputs.contains(output);
	}

	public boolean doesIncludeFiles() {
		return doesInclude(IncludedOutput.files);
	}

	public boolean doesIncludeClasses() {
		return doesInclude(IncludedOutput.classes);
	}

	public boolean doesIncludeMethods() {
		return doesInclude(IncludedOutput.methods);
	}

	public boolean doesIncludeFields() {
		return doesInclude(IncludedOutput.fields);
	}

	public boolean doesIncludeHeritageJars() {
		return doesInclude(IncludedOutput.heritage_jars);
	}

	public boolean doesIncludeAnnotations() {
		return doesInclude(IncludedOutput.annotations);
	}

	public boolean doesIncludeAnalysis() {
		return doesInclude(IncludedOutput.analysis);
	}

	public boolean doesIncludeTags() {
		return doesInclude(IncludedOutput.tags);
	}

	public boolean doesIncludeSummary() {
		return doesInclude(IncludedOutput.summary);
	}

	public boolean isModBlacklisted(String modId) {
		return this.modBlacklist.contains(modId);
	}

	public boolean isDependencyBlacklisted(String modId) {
		return this.dependencyBlacklist.contains(modId);
	}

	public boolean shouldUseDescriptionsInSummary() {
		return this.useDescriptionsInSummary;
	}

	public boolean isLanguageIncluded(String language) {
		return this.languagesToInclude.contains(language);
	}

	public boolean enableCodeParsing() {
		return this.enableCodeParsing;
	}

	public int getThreadsForScanning() {
		int availableCores = Runtime.getRuntime().availableProcessors();
		if(threadsForScanning <= 0 || threadsForScanning > availableCores) {
			threadsForScanning = availableCores;
		}

		return threadsForScanning;
	}

	public int getScanTimeoutSeconds() {
		if(scanTimeoutSeconds <= 0) {
			scanTimeoutSeconds = Integer.MAX_VALUE;
		}
		return scanTimeoutSeconds;
	}

	@Override
	public IConfig setScanTimeoutSeconds(int seconds) {
		this.scanTimeoutSeconds = seconds;
		return this;
	}

	@Override
	public IConfig setThreadsForScanning(int threads) {
		this.threadsForScanning = threads;
		return this;
	}

	@Override
	public IConfig setUseDescriptionsInSummary(boolean useDescriptionsInSummary) {
		this.useDescriptionsInSummary = useDescriptionsInSummary;
		return this;
	}

	@Override
	public IConfig setEnableCodeParsing(boolean enableCodeParsing) {
		this.enableCodeParsing = enableCodeParsing;
		return this;
	}

	@Override
	public IConfig applyPreset(IncludePresets preset) {
		includedOutputs.addAll(preset.outputs);
		modBlacklist.addAll(preset.modBlacklist);
		dependencyBlacklist.addAll(preset.dependencyBlacklist);
		return this;
	}

	@Override
	public IConfig applyPresets(Collection<IncludePresets> presets) {
		for(var preset : presets) {
			applyPreset(preset);
		}
		return this;
	}

	@Override
	public IConfig setDependenciesBlacklist(Collection<String> modIds) {
		dependencyBlacklist.clear();
		dependencyBlacklist.addAll(modIds);
		return this;
	}

	@Override
	public IConfig blacklistDependency(String modId) {
		dependencyBlacklist.add(modId);
		return this;
	}

	@Override
	public IConfig whitelistDependency(String modId) {
		dependencyBlacklist.remove(modId);
		return this;
	}

	@Override
	public IConfig setModsBlacklist(Collection<String> modIds) {
		modBlacklist.clear();
		modBlacklist.addAll(modIds);
		return this;
	}

	@Override
	public IConfig blacklistMod(String modId) {
		modBlacklist.add(modId);
		return this;
	}

	@Override
	public IConfig whitelistMod(String modId) {
		modBlacklist.remove(modId);
		return this;
	}

	@Override
	public IConfig addOutput(Collection<IncludedOutput> outputs) {
		includedOutputs.addAll(outputs);
		return this;
	}

	@Override
	public IConfig addOutput(IncludedOutput output) {
		includedOutputs.add(output);
		return this;
	}

	public IConfig excludeOutput(IncludedOutput output) {
		includedOutputs.remove(output);
		return this;
	}

	@Override
	public IConfig addAllOutputs() {
		includedOutputs.addAll(Arrays.asList(IncludedOutput.values()));
		return this;
	}

	public IConfig excludeAllOutputs() {
		includedOutputs.clear();
		return this;
	}

	@Override
	public IConfig setLanguagesToInclude(Collection<String> languages) {
		languagesToInclude.clear();
		languagesToInclude.addAll(languages);
		return this;
	}

	@Override
	public IConfig addLanguageToInclude(String language) {
		languagesToInclude.add(language);
		return this;
	}

}
