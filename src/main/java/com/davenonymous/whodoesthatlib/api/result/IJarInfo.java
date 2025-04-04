package com.davenonymous.whodoesthatlib.api.result;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.Manifest;

public interface IJarInfo {
	IJarScanner scanner();

	Path jar();

	String getSHA1();

	IJarInfo parentJar();

	Optional<Path> parentJarPath();

	Optional<byte[]> getFileContent(Path path);

	boolean getFileSystem(Consumer<FileSystem> fileSystemConsumer);

	String getShortestCommonPackage();

	List<IClassInfo> getClasses();

	Set<IJarInfo> getNestedJars();

	Manifest getManifest();

	List<Path> files();

	Map<String, Set<ISummaryDescription>> tags();

	<T extends IModResult> T getAnalysisResult(Class<T> resultClass);

	Set<Class<? extends IModResult>> getAnalysisIDs();

	IJarInfo getResource(Path path, Consumer<InputStream> consumer) throws IOException;

	Set<IClassInfo> getAnnotatedClasses(String annotationClassName);

	Set<IMethodInfo> getAnnotatedMethods(String annotationClassName);

	Set<IFieldInfo> getAnnotatedFields(String annotationClassName);

	Set<Type> getUsedTypes();

	Set<String> getCalledMethods();

	Set<String> getLocalizations();

	Map<String, Set<String>> getSummariesForJson();

	Map<ISummaryDescription, List<Object>> getSummaries();

	void addSummary(ISummaryDescription description, Object extraData);

	default void addSummary(ISummaryDescription description) {
		addSummary(description, null);
	}

	default List<String> getTags() {
		return tags().keySet().stream().sorted(Comparator.naturalOrder()).toList();
	}
}
