package com.davenonymous.whodoesthatlib.impl.jar;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeModInfo;
import com.davenonymous.whodoesthatlib.impl.result.neoforge.NeoForgeDependencyInfo;
import com.davenonymous.whodoesthatlib.impl.result.neoforge.NeoForgeJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.neoforge.NeoForgeModInfo;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NeoForgeMod implements IJarAnalyzer {
	@Override
	public void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result) {
		if(!file.toString().equals("/META-INF/neoforge.mods.toml") && !file.toString().equals("/META-INF/mods.toml")) {
			return;
		}

		if(result instanceof NeoForgeJarInfo jarInfo) {
			try {
				String tomlRaw = Files.readString(file);
				TomlParseResult toml = Toml.parse(tomlRaw);

				String license = toml.getString("license", () -> "All rights reserved");
				String loader = toml.getString("modLoader", () -> "javafml");
				String issuesUrl = toml.getString("issueTrackerURL", () -> "");
				jarInfo.setLicense(license);
				jarInfo.setModLoader(loader);
				jarInfo.setIssuesUrl(issuesUrl);

				List<INeoForgeModInfo> mods = new ArrayList<>();
				TomlArray modList = toml.getArray("mods");
				for(int modIndex = 0; modIndex < modList.size(); modIndex++) {
					TomlTable modTable = modList.getTable(modIndex);
					String modId = modTable.getString("modId");
					if(modId == null || modId.isBlank()) {
						continue;
					}

					List<IDependencyInfo> modDependencies = new ArrayList<>();
					if(toml.contains("dependencies." + modId)) {
						TomlArray dependencies = toml.getArray("dependencies." + modId);
						for(int depIndex = 0; depIndex < dependencies.size(); depIndex++) {
							TomlTable depTable = dependencies.getTable(depIndex);
							NeoForgeDependencyInfo depInfo = NeoForgeDependencyInfo.fromToml(depTable);
							if(scanner.config().isDependencyBlacklisted(depInfo.modId())) {
								continue;
							}
							modDependencies.add(depInfo);
						}
					}

					Map<String, Object> customProperties = Collections.emptyMap();
					if(toml.contains("modproperties." + modId)) {
						TomlTable customPropertiesTable = toml.getTable("modproperties." + modId);
						if(customPropertiesTable != null) {
							var propertyMap = customPropertiesTable.toMap();
							if(!propertyMap.isEmpty()) {
								customProperties = propertyMap;
							}
						}
					}

					mods.add(NeoForgeModInfo.fromToml(modTable, modDependencies, customProperties, toml));
				}
				jarInfo.setMods(mods);
			} catch (IOException e) {

			}
		}

	}
}
