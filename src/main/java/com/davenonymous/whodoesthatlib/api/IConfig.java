package com.davenonymous.whodoesthatlib.api;

import com.davenonymous.whodoesthatlib.Config;

import java.util.Collection;
import java.util.Set;

public interface IConfig {
	Set<String> DEFAULT_DEPENDENCY_BLACKLIST = Set.of("minecraft", "forge", "fabricloader", "fabric", "neoforge");
	Set<String> DEFAULT_MOD_BLACKLIST = Set.of("minecraft", "forge", "fabricloader", "fabric", "neoforge");

	boolean doesInclude(IncludedOutput output);

	boolean doesIncludeFiles();

	boolean doesIncludeClasses();

	boolean doesIncludeMethods();

	boolean doesIncludeFields();

	boolean doesIncludeHeritageJars();

	boolean doesIncludeAnnotations();

	boolean doesIncludeAnalysis();

	boolean doesIncludeTags();

	boolean doesIncludeSummary();

	boolean isModBlacklisted(String modId);

	boolean isDependencyBlacklisted(String modId);

	boolean shouldUseDescriptionsInSummary();

	boolean isLanguageIncluded(String language);

	boolean enableCodeParsing();

	int getThreadsForScanning();

	int getScanTimeoutSeconds();

	IConfig setScanTimeoutSeconds(int seconds);

	IConfig setThreadsForScanning(int threads);

	IConfig setUseDescriptionsInSummary(boolean useDescriptionsInSummary);

	IConfig setEnableCodeParsing(boolean enableCodeParsing);

	IConfig applyPreset(IncludePresets preset);

	IConfig applyPresets(Collection<IncludePresets> presets);

	IConfig setDependenciesBlacklist(Collection<String> modIds);

	IConfig blacklistDependency(String modId);

	IConfig whitelistDependency(String modId);

	IConfig setModsBlacklist(Collection<String> modIds);

	IConfig blacklistMod(String modId);

	IConfig whitelistMod(String modId);

	IConfig addOutput(Collection<IncludedOutput> outputs);

	IConfig addOutput(IncludedOutput output);

	IConfig addAllOutputs();

	IConfig setLanguagesToInclude(Collection<String> languages);

	IConfig addLanguageToInclude(String language);



	enum IncludedOutput {
		files,
		classes,
		methods,
		fields,
		heritage_jars,
		annotations,
		analysis,
		tags,
		summary
	}

	enum IncludePresets {
		minimal(Set.of(), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST),
		human(Set.of(IncludedOutput.tags, IncludedOutput.summary), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST),
		report(Set.of(IncludedOutput.analysis, IncludedOutput.tags, IncludedOutput.summary), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST),
		code(
			Set.of(
				IncludedOutput.analysis,
				IncludedOutput.tags,
				IncludedOutput.classes,
				IncludedOutput.methods,
				IncludedOutput.fields,
				IncludedOutput.annotations,
				IncludedOutput.summary
			), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST
		),
		full(
			Set.of(
				IncludedOutput.analysis,
				IncludedOutput.tags,
				IncludedOutput.classes,
				IncludedOutput.methods,
				IncludedOutput.fields,
				IncludedOutput.annotations,
				IncludedOutput.files,
				IncludedOutput.summary
			), Config.DEFAULT_MOD_BLACKLIST, Set.of()
		),
		extreme(Set.of(IncludedOutput.values()), Set.of(), Set.of());

		public final Set<IncludedOutput> outputs;
		public final Set<String> modBlacklist;
		public final Set<String> dependencyBlacklist;

		IncludePresets(Set<IncludedOutput> outputs, Set<String> modBlacklist, Set<String> dependencyBlacklist) {
			this.outputs = outputs;
			this.modBlacklist = modBlacklist;
			this.dependencyBlacklist = dependencyBlacklist;
		}


	}
}
