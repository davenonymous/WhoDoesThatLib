package com.davenonymous.whodoesthatlib;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.analyzers.IModFieldAnalyzer;
import com.davenonymous.whodoesthatlib.api.analyzers.IModFileAnalyzer;
import com.davenonymous.whodoesthatlib.api.analyzers.IModMethodAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeJarInfo;
import com.davenonymous.whodoesthatlib.impl.mod.*;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;
import com.davenonymous.whodoesthatlib.impl.result.ScanResult;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.*;

public class ResultAnalyzer {
	private IJarScanner jarScanner;
	private final IScanResult result;

	private final List<IModClassAnalyzer<?>> classAnalyzers = new ArrayList<>();
	private final List<IModFieldAnalyzer<?>> fieldAnalyzers = new ArrayList<>();
	private final List<IModMethodAnalyzer<?>> methodAnalyzers = new ArrayList<>();
	private final List<IModFileAnalyzer> fileAnalyzers = new ArrayList<>();
	private final List<AnnotationAnalyzer<?, ?>> annotationAnalyzers = new ArrayList<>();

	public ResultAnalyzer(JarScanner jarScanner, ScanResult result) {
		this.jarScanner = jarScanner;
		this.result = result;

		classAnalyzers.add(new InheritanceClassAnalyzer());
		classAnalyzers.add(new MixinClassAnalyzer());
		classAnalyzers.add(new UsedTypesAnalyzer());
		classAnalyzers.add(new CalledMethodsAnalyzer());
		methodAnalyzers.add(new EventListenerAnalyzer());
		fileAnalyzers.add(new ResourceDataAnalyzer());
		fieldAnalyzers.add(new FieldTypeAnalyzer());
		annotationAnalyzers.add(new ClassAnnotationAnalyzer());
		annotationAnalyzers.add(new MethodAnnotationAnalyzer());
		annotationAnalyzers.add(new FieldAnnotationAnalyzer());

		loadCustomAnalyzers();
		performAnalysis();
	}

	private int loadCustomAnalyzers() {
		int count = 0;
		for(IClassInfo classInfo : this.result.classesWithType(IModClassAnalyzer.class)) {
			try {
				Class<?> plugin = Class.forName(classInfo.type().getClassName());
				var instance = plugin.getDeclaredConstructor().newInstance();
				if(instance instanceof IModClassAnalyzer classAnalyzer) {
					classAnalyzers.add(classAnalyzer);
					count++;
				}
				if(instance instanceof IModFieldAnalyzer fieldAnalyzer) {
					fieldAnalyzers.add(fieldAnalyzer);
					count++;
				}
				if(instance instanceof IModMethodAnalyzer methodAnalyzer) {
					methodAnalyzers.add(methodAnalyzer);
					count++;
				}
			} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException | IllegalAccessException e) {
			}
		}

		return count;
	}

	private void performAnalysis() {
		classAnalyzers.forEach(analyzer -> analyzer.onInit(result));
		fieldAnalyzers.forEach(analyzer -> analyzer.onInit(result));
		methodAnalyzers.forEach(analyzer -> analyzer.onInit(result));
		fileAnalyzers.forEach(analyzer -> analyzer.onInit(result));
		annotationAnalyzers.forEach(analyzer -> analyzer.onInit(result));

		for(var jarInfo : result.jars()) {
			IModInfo mod = null;
			if(jarInfo instanceof INeoForgeJarInfo neoForgeJarInfo) {
				mod = neoForgeJarInfo.mods().getFirst();
			}
			classAnalyzers.forEach(analyzer -> analyzer.onJarStart(result, jarInfo));
			fieldAnalyzers.forEach(analyzer -> analyzer.onJarStart(result, jarInfo));
			methodAnalyzers.forEach(analyzer -> analyzer.onJarStart(result, jarInfo));
			fileAnalyzers.forEach(analyzer -> analyzer.onJarStart(result, jarInfo));
			annotationAnalyzers.forEach(analyzer -> analyzer.onJarStart(result, jarInfo));

			for(Path path : jarInfo.files()) {
				for(IModFileAnalyzer analyzer : fileAnalyzers) {
					analyzer.visitFile(result, jarInfo, mod, path);
				}
			}

			String shortestCommonPackage = null;
			for(var classInfo : jarInfo.getClasses()) {
				if(shortestCommonPackage == null) {
					shortestCommonPackage = classInfo.getPackageName();
				} else if(!classInfo.type().getDescriptor().startsWith("Larchitectury_inject_")) {
					String packageName = classInfo.getPackageName();
					// we had shortestCommonPackage="com.foo.bar" and now we encounter "com.foo.baz" -> shortestCommonPackage = "com.foo"
					String[] oldParts = shortestCommonPackage.split("\\.");
					String[] newParts = packageName.split("\\.");
					List<String> newShortestParts = new LinkedList<>();
					for(int i = 0; i < Math.min(oldParts.length, newParts.length); i++) {
						if(!oldParts[i].equals(newParts[i])) {
							break;
						}
						newShortestParts.add(oldParts[i]);
					}
					shortestCommonPackage = String.join(".", newShortestParts);
				}

				fieldAnalyzers.forEach(IModFieldAnalyzer::onClassStart);
				methodAnalyzers.forEach(IModMethodAnalyzer::onClassStart);


				for(IModClassAnalyzer<?> analyzer : classAnalyzers) {
					analyzer.visitClass(result, jarInfo, mod, classInfo);
				}

				for(var analyzer : annotationAnalyzers) {
					if(analyzer instanceof ClassAnnotationAnalyzer classAnalyzer) {
						classAnalyzer.visit(result, jarInfo, mod, classInfo);
					}
				}

				for(var methodInfo : classInfo.methods()) {
					for(IModMethodAnalyzer<?> analyzer : methodAnalyzers) {
						analyzer.visitMethod(result, jarInfo, mod, classInfo, methodInfo);
					}
					for(var analyzer : annotationAnalyzers) {
						if(analyzer instanceof MethodAnnotationAnalyzer methodAnalyzer) {
							methodAnalyzer.visit(result, jarInfo, mod, methodInfo);
						}
					}
				}

				for(var fieldInfo : classInfo.fields()) {
					for(IModFieldAnalyzer<?> analyzer : fieldAnalyzers) {
						analyzer.visitField(result, jarInfo, mod, classInfo, fieldInfo);
					}
					for(var analyzer : annotationAnalyzers) {
						if(analyzer instanceof FieldAnnotationAnalyzer fieldAnalyzer) {
							fieldAnalyzer.visit(result, jarInfo, mod, fieldInfo);
						}
					}
				}

				fieldAnalyzers.forEach(IModFieldAnalyzer::onClassEnd);
				methodAnalyzers.forEach(IModMethodAnalyzer::onClassEnd);
			}
			classAnalyzers.forEach(analyzer -> analyzer.onJarEnd(result, jarInfo));
			fieldAnalyzers.forEach(analyzer -> analyzer.onJarEnd(result, jarInfo));
			methodAnalyzers.forEach(analyzer -> analyzer.onJarEnd(result, jarInfo));
			fileAnalyzers.forEach(analyzer -> analyzer.onJarEnd(result, jarInfo));
			annotationAnalyzers.forEach(analyzer -> analyzer.onJarEnd(result, jarInfo));

			Map<Class<? extends IModResult>, Object> modData = new HashMap<>();
			JsonObject jarJson = new JsonObject();
			for(IModClassAnalyzer<?> analyzer : classAnalyzers) {
				if(analyzer.encodedResult() != JsonNull.INSTANCE) {
					jarJson.add(analyzer.getKey(), analyzer.encodedResult());
				}
				modData.put(analyzer.result().getClass(), analyzer.result());
			}

			for(IModFieldAnalyzer<?> analyzer : fieldAnalyzers) {
				if(analyzer.encodedResult() != JsonNull.INSTANCE) {
					jarJson.add(analyzer.getKey(), analyzer.encodedResult());
				}
				modData.put(analyzer.result().getClass(), analyzer.result());
			}

			for(IModMethodAnalyzer<?> analyzer : methodAnalyzers) {
				if(analyzer.encodedResult() != JsonNull.INSTANCE) {
					jarJson.add(analyzer.getKey(), analyzer.encodedResult());
				}
				modData.put(analyzer.result().getClass(), analyzer.result());
			}

			for(var analyzer : annotationAnalyzers) {
				if(analyzer.encodedResult() != JsonNull.INSTANCE) {
					jarJson.add(analyzer.getKey(), analyzer.encodedResult());
				}
				modData.put(analyzer.result().getClass(), analyzer.result());
			}

			if(jarInfo instanceof JarInfo jarInfoImpl) {
				jarInfoImpl.setAnalysisResult(modData);
				jarInfoImpl.setJsonAnalysisResult(jarJson);
				jarInfoImpl.setShortestBasePackage(shortestCommonPackage != null ? shortestCommonPackage : "");
			}
		}
	}
}
