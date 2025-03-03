package com.davenonymous.whodoesthatlib.impl.serialize;

import com.davenonymous.whodoesthatlib.api.IConfig;
import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.fabric.IFabricJarInfo;
import com.davenonymous.whodoesthatlib.api.result.fabric.IFabricModInfo;
import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeJarInfo;
import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeModInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;
import com.davenonymous.whodoesthatlib.impl.result.neoforge.NeoForgeJarInfo;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;
import java.util.function.Function;

public class SerializerCodecs {

	public static <T> Function<IConfig, T> configWrapCodec(Function<IConfig, T> configSupplier) {
		return (config) -> {
			return configSupplier.apply(config);
		};
	}

	public static Function<IConfig, MapCodec<IAnnotationInfo>> ANNOTATION_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codecs.TYPE_CODEC.fieldOf("class").forGetter(IAnnotationInfo::type),
			Codec.unboundedMap(Codec.STRING, Codecs.OBJECT_VALUE_CODEC).optionalFieldOf("params")
				.forGetter(iAnnotationInfo -> OptionalHelper.optionalOfMap(iAnnotationInfo.params()))
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IMethodInfo>> METHOD_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.STRING.fieldOf("name").forGetter(IMethodInfo::name),
			Codecs.TYPE_CODEC.fieldOf("returnType").forGetter(IMethodInfo::returnType),
			Codecs.TYPE_CODEC.listOf().optionalFieldOf("parameters").forGetter(iMethodInfo -> Codecs.disabler(iMethodInfo.parameters(), true)),
			ANNOTATION_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("annotations")
				.forGetter(iMethodInfo -> Codecs.disabler(iMethodInfo.annotations(), config.doesIncludeAnnotations()))
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IFieldInfo>> FIELD_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.STRING.fieldOf("name").forGetter(IFieldInfo::name),
			Codecs.TYPE_CODEC.fieldOf("type").forGetter(IFieldInfo::type),
			ANNOTATION_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("annotations")
				.forGetter(iFieldInfo -> Codecs.disabler(List.copyOf(iFieldInfo.annotations()), config.doesIncludeAnnotations()))
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IClassInfo>> CLASS_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codecs.TYPE_CODEC.fieldOf("className").forGetter(IClassInfo::type),
			Codecs.TYPE_CODEC.optionalFieldOf("inherits").forGetter(iClassInfo -> Optional.ofNullable(iClassInfo.inherits())),
			Codecs.TYPE_CODEC.listOf().optionalFieldOf("interfaces").forGetter(iClassInfo -> Codecs.disabler(List.copyOf(iClassInfo.interfaces()), true)),
			METHOD_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("methods").forGetter(iClassInfo -> Codecs.disabler(
				List.copyOf(iClassInfo.methods()),
				config.doesIncludeMethods()
			)),
			ANNOTATION_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("annotations")
				.forGetter(iClassInfo -> Codecs.disabler(List.copyOf(iClassInfo.annotations()), config.doesIncludeAnnotations())),
			FIELD_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("fields").forGetter(iClassInfo -> Codecs.disabler(
				List.copyOf(iClassInfo.fields()),
				config.doesIncludeFields()
			))
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IDependencyInfo>> DEPENDENCY_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.STRING.fieldOf("modId").forGetter(IDependencyInfo::modId),
			Codec.BOOL.fieldOf("mandatory").forGetter(IDependencyInfo::mandatory),
			Codec.STRING.optionalFieldOf("versionRange").forGetter(IDependencyInfo::versionRange)
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IFabricModInfo>> FABRIC_MOD_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.STRING.fieldOf("modId").forGetter(IFabricModInfo::modId),
			Codec.STRING.fieldOf("displayName").forGetter(IFabricModInfo::displayName),
			Codec.STRING.fieldOf("description").forGetter(IFabricModInfo::description),
			Codec.STRING.fieldOf("version").forGetter(IFabricModInfo::version),
			Codec.STRING.fieldOf("license").forGetter(IFabricModInfo::license),
			Codec.STRING.optionalFieldOf("authors").forGetter(IFabricModInfo::authors),
			Codec.STRING.optionalFieldOf("logoFile").forGetter(IFabricModInfo::logoFile),
			Codec.STRING.optionalFieldOf("modUrl").forGetter(IFabricModInfo::modUrl),
			Codec.STRING.optionalFieldOf("sourcesUrl").forGetter(IFabricModInfo::sourcesURL),
			DEPENDENCY_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("dependencies")
				.forGetter(iFabricModInfo -> OptionalHelper.optionalOfCollection(iFabricModInfo.dependencies()))
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<INeoForgeModInfo>> NEOFORGE_MOD_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codec.STRING.fieldOf("modId").forGetter(INeoForgeModInfo::modId),
			Codec.STRING.fieldOf("displayName").forGetter(INeoForgeModInfo::displayName),
			Codec.STRING.fieldOf("description").forGetter(INeoForgeModInfo::description),
			Codec.STRING.fieldOf("version").forGetter(INeoForgeModInfo::version),
			Codec.STRING.optionalFieldOf("authors").forGetter(INeoForgeModInfo::authors),
			Codec.STRING.optionalFieldOf("credits").forGetter(INeoForgeModInfo::credits),
			Codec.STRING.optionalFieldOf("logoFile").forGetter(INeoForgeModInfo::logoFile),
			Codec.BOOL.optionalFieldOf("logoBlur").forGetter(INeoForgeModInfo::logoBlur),
			Codec.STRING.optionalFieldOf("modUrl").forGetter(INeoForgeModInfo::modUrl),
			Codec.STRING.optionalFieldOf("updateJSONURL").forGetter(INeoForgeModInfo::updateJSONURL),
			DEPENDENCY_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("dependencies")
				.forGetter(iNeoForgeModInfo -> OptionalHelper.optionalOfCollection(iNeoForgeModInfo.dependencies()))
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IFabricJarInfo>> FABRIC_JAR_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codecs.PATH_FILENAME_CODEC.fieldOf("jar").forGetter(IJarInfo::jar),
			Codec.STRING.fieldOf("sha1").forGetter(IJarInfo::getSHA1),
			Codecs.PATH_FILENAME_CODEC.optionalFieldOf("parentJar").forGetter(IJarInfo::parentJarPath),
			CLASS_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("classes").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.getClasses(), config.doesIncludeClasses())),
			Codecs.PATH_CODEC.listOf().optionalFieldOf("files").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.files(), config.doesIncludeFiles())),

			FABRIC_MOD_INFO_CODEC.apply(config).codec().listOf().fieldOf("mods").forGetter(IFabricJarInfo::mods),
			Codecs.JSON.optionalFieldOf("analysis").forGetter(iJarInfo -> Codecs.disabler(((JarInfo) iJarInfo).getJsonAnalysisResult(), config.doesIncludeAnalysis())),
			Codec.STRING.listOf().optionalFieldOf("tags").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.getTags(), config.doesIncludeTags())),
			Codec.STRING.fieldOf("package").forGetter(IJarInfo::getShortestCommonPackage),
			Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf()).optionalFieldOf("summary").forGetter(iJarInfo -> {
				if(!config.doesIncludeSummary()) {
					return Optional.empty();
				}
				Map<String, Set<String>> raw = iJarInfo.getSummariesForJson();
				Map<String, List<String>> listified = Codecs.mapSetToMapList(raw, Comparator.naturalOrder());
				return OptionalHelper.optionalOfMap(listified);
			})
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<INeoForgeJarInfo>> NEOFORGE_JAR_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codecs.PATH_FILENAME_CODEC.fieldOf("jar").forGetter(IJarInfo::jar),
			Codec.STRING.fieldOf("sha1").forGetter(IJarInfo::getSHA1),
			Codecs.PATH_FILENAME_CODEC.optionalFieldOf("parentJar").forGetter(IJarInfo::parentJarPath),
			CLASS_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("classes").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.getClasses(), config.doesIncludeClasses())),
			Codecs.PATH_CODEC.listOf().optionalFieldOf("files").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.files(), config.doesIncludeFiles())),
			Codec.STRING.fieldOf("modLoader").forGetter(INeoForgeJarInfo::modLoader),
			Codec.STRING.fieldOf("license").forGetter(INeoForgeJarInfo::license),
			Codec.STRING.optionalFieldOf("issuesUrl").forGetter(INeoForgeJarInfo::issuesUrl),
			NEOFORGE_MOD_INFO_CODEC.apply(config).codec().listOf().fieldOf("mods").forGetter(INeoForgeJarInfo::mods),
			Codecs.JSON.optionalFieldOf("analysis").forGetter(iNeoForgeJarInfo -> Codecs.disabler(
				((JarInfo) iNeoForgeJarInfo).getJsonAnalysisResult(),
				config.doesIncludeAnalysis()
			)),
			Codec.STRING.listOf().optionalFieldOf("tags").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.getTags(), config.doesIncludeTags())),
			Codec.STRING.fieldOf("package").forGetter(IJarInfo::getShortestCommonPackage),
			Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf()).optionalFieldOf("summary").forGetter(iJarInfo -> {
				if(!config.doesIncludeSummary()) {
					return Optional.empty();
				}
				Map<String, Set<String>> raw = iJarInfo.getSummariesForJson();
				Map<String, List<String>> listified = Codecs.mapSetToMapList(raw, Comparator.naturalOrder());
				return OptionalHelper.optionalOfMap(listified);
			})
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, MapCodec<IJarInfo>> JAR_INFO_CODEC = configWrapCodec((config) -> RecordCodecBuilder.mapCodec(instance -> {
		return instance.group(
			Codecs.PATH_FILENAME_CODEC.fieldOf("jar").forGetter(IJarInfo::jar),
			Codec.STRING.fieldOf("sha1").forGetter(IJarInfo::getSHA1),
			Codecs.PATH_FILENAME_CODEC.optionalFieldOf("parentJar").forGetter(IJarInfo::parentJarPath),
			CLASS_INFO_CODEC.apply(config).codec().listOf().optionalFieldOf("classes").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.getClasses(), config.doesIncludeClasses())),
			Codecs.PATH_CODEC.listOf().optionalFieldOf("files").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.files(), config.doesIncludeFiles())),
			Codecs.JSON.optionalFieldOf("analysis").forGetter(iNeoForgeJarInfo -> Codecs.disabler(
				((JarInfo) iNeoForgeJarInfo).getJsonAnalysisResult(),
				config.doesIncludeAnalysis()
			)),
			Codec.STRING.listOf().optionalFieldOf("tags").forGetter(iJarInfo -> Codecs.disabler(iJarInfo.getTags(), config.doesIncludeTags())),
			Codec.STRING.fieldOf("package").forGetter(IJarInfo::getShortestCommonPackage),
			Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf()).optionalFieldOf("summary").forGetter(iJarInfo -> {
				if(!config.doesIncludeSummary()) {
					return Optional.empty();
				}
				Map<String, Set<String>> raw = iJarInfo.getSummariesForJson();
				Map<String, List<String>> listified = Codecs.mapSetToMapList(raw, Comparator.naturalOrder());
				return OptionalHelper.optionalOfMap(listified);
			})
		).apply(instance, CodecDevNull::nullCodec);
	}));

	public static Function<IConfig, Codec<IScanResult>> SCAN_RESULT_CODEC = configWrapCodec((config) -> RecordCodecBuilder.create(instance -> {
		return instance.group(
			Codec.either(JAR_INFO_CODEC.apply(config).codec(), Codec.either(NEOFORGE_JAR_INFO_CODEC.apply(config).codec(), FABRIC_JAR_INFO_CODEC.apply(config).codec())).listOf()
				.fieldOf("jars").forGetter(iScanResult -> {
					List<Either<IJarInfo, Either<INeoForgeJarInfo, IFabricJarInfo>>> jars = new ArrayList<>();
					for(var jar : iScanResult.jars()) {
						if(jar instanceof NeoForgeJarInfo) {
							jars.add(Either.right(Either.left((INeoForgeJarInfo) jar)));
						} else if(jar instanceof IFabricJarInfo) {
							jars.add(Either.right(Either.right((IFabricJarInfo) jar)));
						} else {
							jars.add(Either.left(jar));
						}
					}
					return jars;
				})
		).apply(instance, CodecDevNull::nullCodec);
	}));


}
