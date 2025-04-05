package com.davenonymous.whodoesthatlib.api;

import com.davenonymous.whodoesthatlib.Config;

import java.util.Collection;
import java.util.Set;

/**
 * Configuration interface for controlling JAR scanning and analysis behavior.
 * <p>
 * This interface provides methods to configure:
 * <ul>
 *   <li>What types of outputs to include in scan results (files, classes, methods, etc.)</li>
 *   <li>Which mods should be excluded from scanning</li>
 *   <li>Which dependencies should be excluded</li>
 *   <li>Configuration for multi-threading and timeout settings</li>
 *   <li>Language preferences for internationalization</li>
 * </ul>
 * <p>
 * Most methods return the IConfig instance to allow for method chaining.
 */
public interface IConfig {
	/** Default mods to exclude from dependency scanning */
	Set<String> DEFAULT_DEPENDENCY_BLACKLIST = Set.of("minecraft", "forge", "fabricloader", "fabric", "neoforge");
	/** Default mods to exclude from direct scanning */
	Set<String> DEFAULT_MOD_BLACKLIST = Set.of("minecraft", "forge", "fabricloader", "fabric", "neoforge");

	/**
	 * Checks if a specific output type is included in the scan results.
	 *
	 * @param output The output type to check
	 * @return true if the specified output type should be included
	 */
	boolean doesInclude(IncludedOutput output);

	/**
	 * Checks if file information should be included in scan results.
	 *
	 * @return true if files should be included
	 */
	boolean doesIncludeFiles();

	/**
	 * Checks if class information should be included in scan results.
	 *
	 * @return true if classes should be included
	 */
	boolean doesIncludeClasses();

	/**
	 * Checks if method information should be included in scan results.
	 *
	 * @return true if methods should be included
	 */
	boolean doesIncludeMethods();

	/**
	 * Checks if field information should be included in scan results.
	 *
	 * @return true if fields should be included
	 */
	boolean doesIncludeFields();

	/**
	 * Checks if heritage JAR information should be included in scan results.
	 *
	 * @return true if heritage JARs should be included
	 */
	boolean doesIncludeHeritageJars();

	/**
	 * Checks if annotation information should be included in scan results.
	 *
	 * @return true if annotations should be included
	 */
	boolean doesIncludeAnnotations();

	/**
	 * Checks if analysis information should be included in scan results.
	 *
	 * @return true if analysis should be included
	 */
	boolean doesIncludeAnalysis();

	/**
	 * Checks if tag information should be included in scan results.
	 *
	 * @return true if tags should be included
	 */
	boolean doesIncludeTags();

	/**
	 * Checks if summary information should be included in scan results.
	 *
	 * @return true if summary should be included
	 */
	boolean doesIncludeSummary();

	/**
	 * Checks if a mod is blacklisted (excluded from scanning).
	 *
	 * @param modId The mod ID to check
	 * @return true if the mod should be excluded
	 */
	boolean isModBlacklisted(String modId);

	/**
	 * Checks if a dependency is blacklisted (excluded from scanning).
	 *
	 * @param modId The mod ID of the dependency to check
	 * @return true if the dependency should be excluded
	 */
	boolean isDependencyBlacklisted(String modId);

	/**
	 * Checks if descriptions should be included in summary outputs.
	 *
	 * @return true if descriptions should be used in summaries
	 */
	boolean shouldUseDescriptionsInSummary();

	/**
	 * Checks if a specific language is included for internationalization.
	 *
	 * @param language The language code to check (e.g., "en_us")
	 * @return true if the language should be included
	 */
	boolean isLanguageIncluded(String language);

	/**
	 * Checks if code parsing is enabled during scanning.
	 *
	 * @return true if code parsing should be performed
	 */
	boolean enableCodeParsing();

	/**
	 * Gets the number of threads to use for parallel scanning.
	 *
	 * @return the number of threads to use
	 */
	int getThreadsForScanning();

	/**
	 * Gets the timeout in seconds for scan operations.
	 *
	 * @return the timeout in seconds
	 */
	int getScanTimeoutSeconds();

	/**
	 * Sets the timeout in seconds for scan operations.
	 *
	 * @param seconds The timeout in seconds
	 * @return this config instance for method chaining
	 */
	IConfig setScanTimeoutSeconds(int seconds);

	/**
	 * Sets the number of threads to use for parallel scanning.
	 *
	 * @param threads The number of threads
	 * @return this config instance for method chaining
	 */
	IConfig setThreadsForScanning(int threads);

	/**
	 * Sets whether descriptions should be included in summary outputs.
	 *
	 * @param useDescriptionsInSummary Whether to include descriptions
	 * @return this config instance for method chaining
	 */
	IConfig setUseDescriptionsInSummary(boolean useDescriptionsInSummary);

	/**
	 * Sets whether code parsing is enabled during scanning.
	 *
	 * @param enableCodeParsing Whether to enable code parsing
	 * @return this config instance for method chaining
	 */
	IConfig setEnableCodeParsing(boolean enableCodeParsing);

	/**
	 * Applies a predefined configuration preset.
	 *
	 * @param preset The preset to apply
	 * @return this config instance for method chaining
	 */
	IConfig applyPreset(IncludePresets preset);

	/**
	 * Applies multiple predefined configuration presets.
	 *
	 * @param presets The collection of presets to apply
	 * @return this config instance for method chaining
	 */
	IConfig applyPresets(Collection<IncludePresets> presets);

	/**
	 * Sets the blacklist for dependencies.
	 *
	 * @param modIds Collection of mod IDs to blacklist
	 * @return this config instance for method chaining
	 */
	IConfig setDependenciesBlacklist(Collection<String> modIds);

	/**
	 * Adds a mod ID to the dependency blacklist.
	 *
	 * @param modId The mod ID to blacklist
	 * @return this config instance for method chaining
	 */
	IConfig blacklistDependency(String modId);

	/**
	 * Removes a mod ID from the dependency blacklist.
	 *
	 * @param modId The mod ID to whitelist
	 * @return this config instance for method chaining
	 */
	IConfig whitelistDependency(String modId);

	/**
	 * Sets the blacklist for mods.
	 *
	 * @param modIds Collection of mod IDs to blacklist
	 * @return this config instance for method chaining
	 */
	IConfig setModsBlacklist(Collection<String> modIds);

	/**
	 * Adds a mod ID to the mod blacklist.
	 *
	 * @param modId The mod ID to blacklist
	 * @return this config instance for method chaining
	 */
	IConfig blacklistMod(String modId);

	/**
	 * Removes a mod ID from the mod blacklist.
	 *
	 * @param modId The mod ID to whitelist
	 * @return this config instance for method chaining
	 */
	IConfig whitelistMod(String modId);

	/**
	 * Adds multiple output types to include in scan results.
	 *
	 * @param outputs Collection of output types to include
	 * @return this config instance for method chaining
	 */
	IConfig addOutput(Collection<IncludedOutput> outputs);

	/**
	 * Adds a single output type to include in scan results.
	 *
	 * @param output The output type to include
	 * @return this config instance for method chaining
	 */
	IConfig addOutput(IncludedOutput output);

	/**
	 * Includes all possible output types in scan results.
	 *
	 * @return this config instance for method chaining
	 */
	IConfig addAllOutputs();

	/**
	 * Sets the languages to include for internationalization.
	 *
	 * @param languages Collection of language codes to include
	 * @return this config instance for method chaining
	 */
	IConfig setLanguagesToInclude(Collection<String> languages);

	/**
	 * Adds a language to include for internationalization.
	 *
	 * @param language The language code to include (e.g., "en_us")
	 * @return this config instance for method chaining
	 */
	IConfig addLanguageToInclude(String language);

	/**
	 * Defines the types of outputs that can be included in scan results.
	 */
	enum IncludedOutput {
		/** File information */
		files,
		/** Class information */
		classes,
		/** Method information */
		methods,
		/** Field information */
		fields,
		/** Heritage JAR information */
		heritage_jars,
		/** Annotation information */
		annotations,
		/** Analysis information */
		analysis,
		/** Tag information */
		tags,
		/** Summary information */
		summary
	}

	/**
	 * Predefined configuration presets for common use cases.
	 */
	enum IncludePresets {
		/**
		 * Minimal configuration with no outputs.
		 * Uses default mod and dependency blacklists.
		 */
		minimal(Set.of(), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST),

		/**
		 * Human-friendly configuration that includes tags and summary.
		 * Uses default mod and dependency blacklists.
		 */
		human(Set.of(IncludedOutput.tags, IncludedOutput.summary), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST),

		/**
		 * Report configuration that includes analysis, tags, and summary.
		 * Uses default mod and dependency blacklists.
		 */
		report(Set.of(IncludedOutput.analysis, IncludedOutput.tags, IncludedOutput.summary), Config.DEFAULT_MOD_BLACKLIST, Config.DEFAULT_DEPENDENCY_BLACKLIST),

		/**
		 * Code-focused configuration that includes analysis, tags, classes, methods, fields, annotations, and summary.
		 * Uses default mod and dependency blacklists.
		 */
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

		/**
		 * Full configuration that includes all outputs except heritage_jars.
		 * Uses default mod blacklist but no dependency blacklist.
		 */
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

		/**
		 * Extreme configuration that includes all outputs.
		 * No mod or dependency blacklists.
		 */
		extreme(Set.of(IncludedOutput.values()), Set.of(), Set.of());

		/** The output types included in this preset */
		public final Set<IncludedOutput> outputs;
		/** The mod blacklist for this preset */
		public final Set<String> modBlacklist;
		/** The dependency blacklist for this preset */
		public final Set<String> dependencyBlacklist;

		IncludePresets(Set<IncludedOutput> outputs, Set<String> modBlacklist, Set<String> dependencyBlacklist) {
			this.outputs = outputs;
			this.modBlacklist = modBlacklist;
			this.dependencyBlacklist = dependencyBlacklist;
		}
	}
}
