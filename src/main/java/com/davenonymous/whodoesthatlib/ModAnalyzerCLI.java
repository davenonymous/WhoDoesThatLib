package com.davenonymous.whodoesthatlib;

import com.davenonymous.whodoesthatlib.api.IConfig;
import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModdedJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.GsonHelper;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

@CommandLine.Command(
	name = "scan", mixinStandardHelpOptions = true,
	description = "Scans the given folders for jars and generates a JSON summary of the mods found.",
	version = "1.0", showDefaultValues = true
)
public class ModAnalyzerCLI implements Callable<Integer> {

	@CommandLine.Option(description = "The folders/files to scan for jars.", names = {"-j", "--jars"})
	private List<Path> pathsToScan;

	@CommandLine.Option(description = "The output file to write the JSON summary to.", names = {"-o", "--output"})
	private File outputFile = new File("wdt.json");

	@CommandLine.Option(description = "The folders/files to scan for heritage data.", names = {"-s", "--supers"}, required = true)
	private List<Path> heritagePaths;

	@CommandLine.Option(description = "The folders/files to load analysis descriptors from.", names = {"-c", "--config"})
	private List<Path> analysisDescriptorConfigPaths = List.of(Path.of("./config.d"));

	@CommandLine.Option(description = "Presets to include in the generated JSON [${COMPLETION-CANDIDATES}]", names = {"-p", "--presets"})
	private Set<IConfig.IncludePresets> includedPresets = Set.of();

	@CommandLine.Option(description = "Additional data to include in the generated JSON [${COMPLETION-CANDIDATES}]", names = {"-i", "--include"})
	private Set<IConfig.IncludedOutput> includedOutputs = Set.of();

	@CommandLine.Option(description = "Dependencies to ignore", names = {"-d", "--ignore-dependencies"})
	private Set<String> ignoredDependencies = Set.of("minecraft", "neoforge");

	@CommandLine.Option(description = "Jars containing these mod ids will be exempt from output", names = {"-m", "--ignore-mods"})
	private Set<String> ignoredMods = Set.of();

	@CommandLine.Option(description = "Use descriptions in the summary", names = {"-u", "--use-descriptions"})
	private boolean useDescriptionsInSummary = false;

	public static void main(String[] args) {
		int exitCode = new CommandLine(new ModAnalyzerCLI())
			.setCaseInsensitiveEnumValuesAllowed(true)
			.execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() {
		try {
			final long scanStartTime = System.nanoTime();
			IJarScanner scanner = IJarScanner.get();
			scanner.config().addOutput(includedOutputs);
			scanner.config().setDependenciesBlacklist(ignoredDependencies);
			scanner.config().setModsBlacklist(ignoredMods);
			scanner.config().applyPresets(includedPresets);
			scanner.config().setUseDescriptionsInSummary(useDescriptionsInSummary);

			scanner.addAnalysisPath(pathsToScan);
			scanner.addHeritagePath(heritagePaths);
			scanner.addDescriptorConfigPath(analysisDescriptorConfigPaths);

			scanner.loadDescriptors();

			scanner.addProgressListener((progress, event) -> {
				System.out.printf("%5.01f%% %3d/%3d/%3d, %3d: %s\n",
					progress.getProgress() * 100,
					progress.scannedJars(), progress.analyzedJars(),
					progress.totalJars(), progress.foundMods(), event);
			});

			IScanResult result = scanner.process();

			System.out.printf("Processed %d jars in %d ms\n", result.jars().size(), (System.nanoTime() - scanStartTime) / 1000000);
			ArrayList<IJarInfo> sorted = new ArrayList<>(result.jars());
			sorted.sort(Comparator.comparing(jarInfo -> {
				var jarName = jarInfo.jar().toString();
				return jarName.substring(jarName.lastIndexOf(File.separator) + 1, jarName.length() - 4);
			}));


//			for(var jar : sorted) {
//				if(jar instanceof IModdedJarInfo<?>) {
//					continue;
//				}
//				System.out.printf("Jar: %s [%s] %s\n", jar.jar().getFileName(), jar.parentJar() == null ? "-" : jar.actualJar().jar().getFileName(), jar.getTags());
//			}

			final long jsonEncodeStartTime = System.nanoTime();
			var jsonResult = result.asJson();
			System.out.printf("Encoded to JSON in %d ms\n", (System.nanoTime() - jsonEncodeStartTime) / 1000000);
			jsonResult.ifPresent(json -> {
				final long jsonWriteStartTime = System.nanoTime();
				try {
					Files.writeString(outputFile.toPath(), GsonHelper.toStableString(json));
					System.out.printf("Written to %s in %d ms\n", outputFile, (System.nanoTime() - jsonWriteStartTime) / 1000000);
				} catch (IOException e) {
					System.out.println("Could not write json: " + e.getMessage());
				}
			});

		} catch (IOException e) {
			System.out.println("Unable to process jars: " + e.getMessage());
			e.printStackTrace();
		}

		return 0;
	}
}
