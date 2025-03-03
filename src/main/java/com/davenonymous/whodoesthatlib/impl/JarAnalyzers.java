package com.davenonymous.whodoesthatlib.impl;

import com.davenonymous.whodoesthatlib.JarScanner;
import com.davenonymous.whodoesthatlib.api.IJarAnalyzerRegistry;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.jar.ASMClassData;
import com.davenonymous.whodoesthatlib.impl.jar.AllFiles;
import com.davenonymous.whodoesthatlib.impl.jar.FabricMod;
import com.davenonymous.whodoesthatlib.impl.jar.NeoForgeMod;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;
import com.davenonymous.whodoesthatlib.impl.result.fabric.FabricJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.neoforge.NeoForgeJarInfo;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class JarAnalyzers implements IJarAnalyzerRegistry {
	JarScanner jarScanner;
	List<IJarAnalyzer> analyzers = new ArrayList<>();
	IJarAnalyzer asmClassData = new ASMClassData();

	public JarAnalyzers(JarScanner jarScanner) {
		this.jarScanner = jarScanner;
		register(asmClassData);
		register(new NeoForgeMod());
		register(new FabricMod());
		register(new AllFiles());
	}

	public void register(IJarAnalyzer analyzer) {
		analyzers.add(analyzer);
	}

	public IJarInfo runHeritageOnly(Path file, FileSystem fs) {
		JarInfo result = new JarInfo(file);
		try {
			Path rootInJar = fs.getPath("/");
			Files.walkFileTree(
				rootInJar, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
						asmClassData.visitFileInJar(jarScanner, fs, file, attrs, result);
						return FileVisitResult.CONTINUE;
					}
				}
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}

	public IJarInfo runAll(Path file, FileSystem fs) {
		IJarInfo result;
		Path neoForgeModsToml = fs.getPath("/META-INF/neoforge.mods.toml");
		Path forgeModsToml = fs.getPath("/META-INF/mods.toml");
		Path fabricModJson = fs.getPath("/fabric.mod.json");
		if(Files.exists(neoForgeModsToml) || Files.exists(forgeModsToml)) {
			result = new NeoForgeJarInfo(file).setJarScanner(jarScanner);
		} else if(Files.exists(fabricModJson)) {
			result = new FabricJarInfo(file).setJarScanner(jarScanner);
		} else {
			result = new JarInfo(file).setJarScanner(jarScanner);
		}

		try {
			Path rootInJar = fs.getPath("/");
			Files.walkFileTree(
				rootInJar, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
						analyzers.forEach(iJarAnalyzer -> iJarAnalyzer.visitFileInJar(jarScanner, fs, file, attrs, result));
						return FileVisitResult.CONTINUE;
					}
				}
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
}
