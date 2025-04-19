package com.davenonymous.whodoesthatlib.api;

import com.davenonymous.whodoesthatlib.JarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IModAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.*;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * The main interface for the WhoDoesThat library.<br/>
 * <p>
 * Get an instance of this interface using {@link IJarScanner#get()}.<br/><br/>
 * <p>
 * Generally you will want to follow these steps:<br>
 * <p>
 * 1. Add paths to scan using {@link #addAnalysisPath(Path)} or {@link #addAnalysisPath(Collection)}.<br/>
 * 2. Add paths to scan for heritage using {@link #addHeritagePath(Path)} or {@link #addHeritagePath(Collection)}.<br/>
 * This is strictly speaking optional, but will provide better results everytime inheritance is involved.<br/>
 * If you don't provide heritage paths, the scanner will only be able to detect inheritance if the parent class is in the analysis paths.<br/>
 * 3. Add paths to load descriptor configurations from using {@link #addDescriptorConfigPath(Path)} or {@link #addDescriptorConfigPath(Collection)}.<br/>
 * These are used to configure the scanner to detect specific things in the jars.<br/>
 * 4. Optionally register custom descriptor types using {@link #registerDescriptorType(String, BiFunction)}.<br/>
 * 5. Load the descriptors using {@link #loadDescriptors()}.<br/>
 * 6. Get and configure the {@link IConfig} instance using {@link IConfig#get()}.<br/>
 * 7. Process the jars using {@link #process()}.<br/>
 * This will return a {@link IScanResult} which contains all gathered information about the jars.<br/>
 */
public interface IJarScanner {
	/**
	 * Returns a new instance of the jar scanner.<br/>
	 * This is the main entry point for the library.<br/>
	 *
	 * @return A new instance of the jar scanner.
	 */
	static IJarScanner get() {
		var defaultScanner = new JarScanner();
		defaultScanner.registerDescriptorType(InheritanceDescription.ID, InheritanceDescription::new);
		defaultScanner.registerDescriptorType(EventDescription.ID, EventDescription::new);
		defaultScanner.registerDescriptorType(MixinDescription.ID, MixinDescription::new);
		defaultScanner.registerDescriptorType(FileGlobDescription.ID, FileGlobDescription::new);
		defaultScanner.registerDescriptorType(FieldTypeDescription.ID, FieldTypeDescription::new);
		defaultScanner.registerDescriptorType(AnnotationDescription.ID, AnnotationDescription::new);
		defaultScanner.registerDescriptorType(UsedTypeDescription.ID, UsedTypeDescription::new);
		defaultScanner.registerDescriptorType(CalledMethodDescription.ID, CalledMethodDescription::new);
		return defaultScanner;
	}

	/**
	 * Returns the current configuration for the scanner.<br/>
	 * <p>
	 * This is the default configuration and can be used to further configure the scanner.<br/>
	 * See {@link IConfig} for more information about the configuration options.<br/>
	 *
	 * @return The current configuration of the scanner.
	 */
	IConfig config();

	/**
	 * Returns the jar analyzers registry.<br/>
	 * <p>
	 * This is used to register custom jar analyzers.<br/>
	 * See {@link IJarAnalyzerRegistry} for more information about how to implement a custom jar analyzer.<br/>
	 *
	 * @return The jar analyzers registry.
	 */
	IJarAnalyzerRegistry jarAnalyzers();

	/**
	 * Adds a path to scan for jars.
	 *
	 * @param path The path to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addAnalysisPath(Path path);

	/**
	 * Adds a collection of paths to scan for jars.
	 *
	 * @param paths The paths to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addAnalysisPath(Collection<Path> paths);

	/**
	 * Adds a collection of paths to scan for jars.
	 *
	 * @param paths The paths to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addAnalysisPath(Path... paths);

	/**
	 * Adds a path to scan for heritage data.<br/>
	 * This is strictly speaking optional, but will provide better results everytime inheritance is involved.<br/>
	 * If you don't provide heritage paths, the scanner will only be able to detect inheritance if the parent class is in the analysis paths.<br/>
	 * You probably want to add a path to a neoforge jar here.
	 *
	 * @param path The path to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addHeritagePath(Path path);

	/**
	 * Adds a collection of paths to scan for heritage data.<br/>
	 * This is strictly speaking optional, but will provide better results everytime inheritance is involved.<br/>
	 * If you don't provide heritage paths, the scanner will only be able to detect inheritance if the parent class is in the analysis paths.<br/>
	 * You probably want to add a path to a neoforge jar here.
	 *
	 * @param paths The paths to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addHeritagePath(Collection<Path> paths);

	/**
	 * Adds a collection of paths to scan for heritage data.<br/>
	 * This is strictly speaking optional, but will provide better results everytime inheritance is involved.<br/>
	 * If you don't provide heritage paths, the scanner will only be able to detect inheritance if the parent class is in the analysis paths.<br/>
	 * You probably want to add a path to a neoforge jar here.
	 *
	 * @param paths The paths to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addHeritagePath(Path... paths);

	/**
	 * Adds a path to load descriptor configurations from.<br/>
	 * These are yaml files used to configure the scanner to detect specific things in the jars.<br/>
	 * More information about the descriptor configuration format can be found in the library guide.<br/>
	 * <p>
	 * Make sure to call {@link #loadDescriptors()} after adding the paths and before {@link #process()}.
	 *
	 * @param path The path to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addDescriptorConfigPath(Path path);


	/**
	 * Adds a collection of paths to load descriptor configurations from.<br/>
	 * These are yaml files used to configure the scanner to detect specific things in the jars.<br/>
	 * More information about the descriptor configuration format can be found in the library guide.<br/>
	 * <p>
	 * Make sure to call {@link #loadDescriptors()} after adding the paths and before {@link #process()}.
	 *
	 * @param paths The paths to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addDescriptorConfigPath(Collection<Path> paths);


	/**
	 * Adds a collection of paths to load descriptor configurations from.<br/>
	 * These are yaml files used to configure the scanner to detect specific things in the jars.<br/>
	 * More information about the descriptor configuration format can be found in the library guide.<br/>
	 * <p>
	 * Make sure to call {@link #loadDescriptors()} after adding the paths and before {@link #process()}.
	 *
	 * @param paths The paths to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addDescriptorConfigPath(Path... paths);


	/**
	 * Registers a custom descriptor type.<br/>
	 * <p>
	 * Custom descriptors must implement {@link ISummaryDescription} and have a unique config key.<br/>
	 * They are used to detect specific things in the jars and can be configured using the descriptor configuration files.<br/><br/>
	 * You should not implement the {@link ISummaryDescription} interface directly, but extend {@link AbstractSummaryDescription} instead.<br/>
	 * Then the only actual code that is required to be implemented is the constructor that takes the config key
	 * and a map of config entries as loaded from the yaml file.<br/><br/>
	 * <p>
	 * Your implementation of {@link IModAnalyzer} will then be able to retrieve all loaded descriptors using {@link IScanResult#getSummaryDescriptions(String configKey)}.
	 *
	 * @param configKey         The config key to register the descriptor for.
	 * @param descriptorFactory The factory to create the descriptor. Usually the constructor of your implementation of {@link AbstractSummaryDescription}.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner registerDescriptorType(String configKey, BiFunction<String, Map<String, Object>, ISummaryDescription> descriptorFactory);


	/**
	 * Loads the descriptors from the configured paths.<br/>
	 * This will load all descriptors from the paths added using {@link #addDescriptorConfigPath(Path)} or {@link #addDescriptorConfigPath(Collection)}.
	 * Make sure to call this method before {@link #process()}.<br/><br/>
	 * Custom descriptors can be registered using {@link #registerDescriptorType(String, BiFunction)}, but they must be registered before this method is called.
	 *
	 * @return This instance of {@link IJarScanner} for chaining.
	 * @throws IOException If an error occurs while loading the descriptors.
	 */
	IJarScanner loadDescriptors() throws IOException;


	/**
	 * Adds a progress listener to the scanner.<br/>
	 * This will be called during the scanning process on various steps.<br/>
	 *
	 * @param listener The progress listener to add.
	 * @return This instance of {@link IJarScanner} for chaining.
	 */
	IJarScanner addProgressListener(IScanProgressListener listener);

	/**
	 * Processes the jars and returns the result.<br/>
	 * This will scan all jars added using {@link #addAnalysisPath(Path)} or {@link #addAnalysisPath(Collection)}.
	 *
	 * @return An {@link IScanResult} containing all gathered information about the jars.<br/>
	 * @throws IOException If an error occurs while processing the jars.
	 */
	IScanResult process() throws IOException;
}
