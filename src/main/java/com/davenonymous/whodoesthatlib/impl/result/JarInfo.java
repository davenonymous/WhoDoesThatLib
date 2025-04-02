package com.davenonymous.whodoesthatlib.impl.result;

import com.davenonymous.whodoesthatlib.JarScanner;
import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import com.davenonymous.whodoesthatlib.impl.FileHash;
import com.davenonymous.whodoesthatlib.impl.serialize.OptionalHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.Manifest;

public class JarInfo implements IJarInfo {
	Path jarPath;
	List<IClassInfo> classes;
	List<Path> files;
	List<Path> dataResources;
	List<Path> assetResources;
	Map<IAnnotationInfo, Set<IClassInfo>> annotatedClasses;
	Map<IAnnotationInfo, Set<IMethodInfo>> annotatedMethods;
	Map<IAnnotationInfo, Set<IFieldInfo>> annotatedFields;
	Map<Class<? extends IModResult>, Object> analysisResult;
	Map<String, Set<ISummaryDescription>> tags;
	JsonElement jsonResult;
	IJarInfo parentJar;
	Set<Type> usedTypes;
	Set<String> calledMethods;
	Set<String> localizations;
	String sha1;
	JarScanner jarScanner;

	Map<String, Set<String>> summariesForJson; // (resultCategory -> List of translationKeys)
	Map<ISummaryDescription, List<Object>> summaries;
	String shortestBasePackage;
	private Manifest manifest;


	public JarInfo(Path jarPath) {
		this.jarPath = jarPath;
		this.classes = new ArrayList<>();
		this.files = new ArrayList<>();
		this.dataResources = new ArrayList<>();
		this.assetResources = new ArrayList<>();
		this.annotatedClasses = new HashMap<>();
		this.annotatedMethods = new HashMap<>();
		this.annotatedFields = new HashMap<>();
		this.summariesForJson = new HashMap<>();
		this.summaries = new HashMap<>();
		this.tags = new HashMap<>();
		this.usedTypes = new HashSet<>();
		this.calledMethods = new HashSet<>();
		this.localizations = new HashSet<>();
		this.shortestBasePackage = "";
		this.parentJar = null;
		try {
			this.sha1 = FileHash.calcSHA1(jarPath.toFile());
		} catch (Exception e) {
			this.sha1 = "";
		}
	}

	public JarInfo setJarScanner(JarScanner jarScanner) {
		this.jarScanner = jarScanner;
		return this;
	}

	@Override
	public IJarScanner scanner() {
		return this.jarScanner;
	}

	@Override
	public Optional<byte[]> getFileContent(Path path) {
		return getFileContent(this, path);
	}

	private Optional<byte[]> getFileContent(IJarInfo jarInfo, Path path) {
		OptionalHelper.Builder<byte[]> result = new OptionalHelper.Builder<>();
		if(jarInfo.parentJar() == null) {
			try(var fs = FileSystems.newFileSystem(jarInfo.jar())) {
				result.value(Files.readAllBytes(fs.getPath(path.toString())));
			} catch (IOException e) {
			}
		} else {
			try {
				var fs = FileSystems.newFileSystem(jarInfo.parentJar().jar());
				Path fsPath = fs.getPath(jarInfo.jar().toString());
				var nestedFs = FileSystems.newFileSystem(fsPath);
				result.value(Files.readAllBytes(nestedFs.getPath(path.toString())));
				nestedFs.close();
				fs.close();
			} catch (Exception e) {
			}
		}

		return result.build();
	}

	@Override
	public boolean getFileSystem(Consumer<FileSystem> fileSystemConsumer) {
		return getFileSystem(this, fileSystemConsumer);
	}

	private boolean getFileSystem(IJarInfo jarInfo, Consumer<FileSystem> fileSystemConsumer) {
		if(jarInfo.parentJar() == null) {
			try(var fs = FileSystems.newFileSystem(jarInfo.jar())) {
				fileSystemConsumer.accept(fs);
				return true;
			} catch (IOException e) {
			}
		} else {
			try(var fs = FileSystems.newFileSystem(jarInfo.parentJar().jar())) {
				Path fsPath = fs.getPath(jarInfo.jar().toString());
				try(var nestedFs = FileSystems.newFileSystem(fsPath)) {
					fileSystemConsumer.accept(nestedFs);
					return true;
				} catch (IOException e) {
				}

			} catch (IOException e) {
			}
		}

		return false;
	}

	@Override
	public Map<String, Set<ISummaryDescription>> tags() {
		return this.tags;
	}

	private <T> Set<T> getAnnotated(Map<IAnnotationInfo, Set<T>> annotatedElements, String annotationClassName) {
		Set<T> result = new HashSet<>();
		for(IAnnotationInfo annotation : annotatedElements.keySet()) {
			if(!annotation.type().getClassName().equals(annotationClassName)) {
				continue;
			}
			result.addAll(annotatedElements.get(annotation));
		}
		return result;
	}

	@Override
	public IJarInfo parentJar() {
		return parentJar;
	}

	@Override
	public Optional<Path> parentJarPath() {
		if(parentJar == null) {
			return Optional.empty();
		}
		return Optional.of(parentJar.jar());
	}

	@Override
	public String getShortestCommonPackage() {
		return this.shortestBasePackage;
	}

	@Override
	public <T extends IModResult> T getAnalysisResult(Class<T> resultClass) {
		//noinspection unchecked
		return (T) analysisResult.get(resultClass);
	}

	@Override
	public void addSummary(ISummaryDescription description, Object extraData) {
		String summaryEntry = jarScanner.config().shouldUseDescriptionsInSummary() ? description.description() : description.resultId();
		this.summariesForJson.computeIfAbsent(description.resultCategory(), k -> new HashSet<>()).add(summaryEntry);
		this.summaries.computeIfAbsent(description, k -> new ArrayList<>()).add(extraData);
		description.tags().forEach(tag -> this.tags.computeIfAbsent(tag, k -> new HashSet<>()).add(description));
	}

	@Override
	public Map<ISummaryDescription, List<Object>> getSummaries() {
		return this.summaries;
	}

	@Override
	public Map<String, Set<String>> getSummariesForJson() {
		return this.summariesForJson;
	}

	public Set<Class<? extends IModResult>> getAnalysisIDs() {
		return analysisResult.keySet();
	}

	public Set<IClassInfo> getAnnotatedClasses(String annotationClassName) {
		return getAnnotated(annotatedClasses, annotationClassName);
	}

	public Set<IMethodInfo> getAnnotatedMethods(String annotationClassName) {
		return getAnnotated(annotatedMethods, annotationClassName);
	}

	public Set<IFieldInfo> getAnnotatedFields(String annotationClassName) {
		return getAnnotated(annotatedFields, annotationClassName);
	}

	@Override
	public IJarInfo getResource(Path path, Consumer<InputStream> consumer) throws IOException {
		try(var fs = FileSystems.newFileSystem(jarPath)) {
			Path rootInJar = fs.getPath(path.toString());
			try(var inputStream = Files.newInputStream(rootInJar, StandardOpenOption.READ)) {
				consumer.accept(inputStream);
			}
		}
		return this;
	}

	public String getSHA1() {
		return sha1;
	}

	public Path jar() {
		return this.jarPath;
	}

	public JarInfo addClass(IClassInfo classInfo) {
		this.classes.add(classInfo);
		this.usedTypes.addAll(classInfo.usedTypes());
		this.calledMethods.addAll(classInfo.calledMethods());

		for(var info : classInfo.annotations()) {
			annotatedClasses.computeIfAbsent(info, k -> new HashSet<>()).add(classInfo);
		}

		for(var info : classInfo.methods()) {
			for(var annotation : info.annotations()) {
				annotatedMethods.computeIfAbsent(annotation, k -> new HashSet<>()).add(info);
			}
		}

		for(var info : classInfo.fields()) {
			for(var annotation : info.annotations()) {
				annotatedFields.computeIfAbsent(annotation, k -> new HashSet<>()).add(info);
			}
		}

		return this;
	}

	@Override
	public Set<Type> getUsedTypes() {
		return usedTypes;
	}

	@Override
	public Set<String> getCalledMethods() {
		return calledMethods;
	}

	public List<IClassInfo> getClasses() {
		return classes;
	}

	public JarInfo addFile(Path path) {
		this.files.add(path);
		return this;
	}

	@Override
	public List<Path> files() {
		return files;
	}

	public Optional<JsonElement> getJsonAnalysisResult() {
		return Optional.ofNullable(jsonResult);
	}

	public JarInfo setAnalysisResult(Map<Class<? extends IModResult>, Object> analysisResult) {
		this.analysisResult = analysisResult;
		return this;
	}

	public void setJsonAnalysisResult(JsonObject jarJson) {
		this.jsonResult = jarJson;
	}

	public void setParentJar(IJarInfo parentJar) {
		this.parentJar = parentJar;
	}

	public void setShortestBasePackage(String shortestBasePackage) {
		this.shortestBasePackage = shortestBasePackage;
	}

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	public Manifest getManifest() {
		return manifest;
	}

	@Override
	public Set<String> getLocalizations() {
		return localizations;
	}

	public JarInfo addLocalization(String localization) {
		this.localizations.add(localization);
		return this;
	}
}
