package com.davenonymous.whodoesthatlib.impl.result;

import com.davenonymous.whodoesthatlib.JarScanner;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.impl.serialize.SerializerCodecs;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.stream.Collectors;

public class ScanResult implements IScanResult {
	List<IJarInfo> jars;
	Map<String, Set<String>> parentToChildren;
	Map<String, Set<IClassInfo>> parentToChildrenInfo;
	Map<String, Set<String>> classToParents;
	Map<String, Set<Type>> classToParentsInfo;
	JarScanner jarScanner;

	public ScanResult(JarScanner jarScanner, Collection<IJarInfo> analyzedJars, Collection<IJarInfo> heritageJars) {
		this.jarScanner = jarScanner;
		this.jars = new ArrayList<>(analyzedJars);

		if(jarScanner.config().doesIncludeHeritageJars()) {
			this.jars.addAll(heritageJars);
		}

		this.jars.sort(Comparator.comparing(jar -> jar.jar().getFileName().toString().toLowerCase(Locale.ROOT)));

		parentToChildren = new HashMap<>();
		parentToChildrenInfo = new HashMap<>();
		classToParents = new HashMap<>();
		classToParentsInfo = new HashMap<>();

		LinkedList<IJarInfo> allJars = new LinkedList<>(analyzedJars);
		allJars.addAll(heritageJars);
		for(IJarInfo jar : allJars) {
			for(IClassInfo classInfo : jar.getClasses()) {
				if(classInfo.inherits() == null) {
					continue;
				}

				String className = classInfo.type().getClassName();
				String parentName = classInfo.inherits().getClassName();

				classToParents.computeIfAbsent(className, k -> new HashSet<>()).add(parentName);
				classToParentsInfo.computeIfAbsent(className, k -> new HashSet<>()).add(classInfo.inherits());
				parentToChildren.computeIfAbsent(parentName, k -> new HashSet<>()).add(className);
				parentToChildrenInfo.computeIfAbsent(parentName, k -> new HashSet<>()).add(classInfo);


				for(Type interfaceType : classInfo.interfaces()) {
					String interfaceName = interfaceType.getClassName();
					classToParents.computeIfAbsent(className, k -> new HashSet<>()).add(interfaceName);
					parentToChildren.computeIfAbsent(interfaceName, k -> new HashSet<>()).add(className);
					parentToChildrenInfo.computeIfAbsent(interfaceName, k -> new HashSet<>()).add(classInfo);
					classToParentsInfo.computeIfAbsent(className, k -> new HashSet<>()).add(interfaceType);
				}
			}
		}

		// Second pass: add all ancestor relationships
		boolean changed;
		do {
			changed = false;
			for(Map.Entry<String, Set<String>> entry : classToParents.entrySet()) {
				String className = entry.getKey();
				Set<String> parents = new HashSet<>(entry.getValue());

				for(String parent : parents) {
					Set<String> grandParents = classToParents.getOrDefault(parent, Collections.emptySet());
					if(entry.getValue().addAll(grandParents)) {
						changed = true;

						// Update parentToChildren for reverse lookup
						for(String grandParent : grandParents) {
							parentToChildren.computeIfAbsent(grandParent, k -> new HashSet<>()).add(className);
							parentToChildrenInfo.computeIfAbsent(grandParent, k -> new HashSet<>()).addAll(parentToChildrenInfo.get(parent));
						}
					}
				}
			}
		} while(changed);
	}

	public Map<String, List<ISummaryDescription>> getDescriptors() {
		return jarScanner.descriptors;
	}

	@Override
	public <T extends ISummaryDescription> List<T> getSummaryDescriptions(String summaryType) {
		if(jarScanner.descriptors == null || jarScanner.descriptors.isEmpty()) {
			return Collections.emptyList();
		}
		if(!jarScanner.descriptors.containsKey(summaryType)) {
			return Collections.emptyList();
		}

		//noinspection unchecked
		return (List<T>) jarScanner.descriptors.get(summaryType);
	}

	@Override
	public List<ISummaryDescription> getSummaryDescriptionsByTag(String tag) {
		if(jarScanner.descriptors == null || jarScanner.descriptors.isEmpty()) {
			return Collections.emptyList();
		}

		return jarScanner.getSummaryDescriptionsByTag(tag);
	}

	@Override
	public Map<ISummaryDescription, Set<IJarInfo>> getUsedDescriptors() {
		Map<ISummaryDescription, Set<IJarInfo>> summariesInJars = new HashMap<>();
		for(IJarInfo jar : jars()) {
			Set<ISummaryDescription> descriptions = jar.getSummaries().keySet();
			for(ISummaryDescription description : descriptions) {
				summariesInJars.computeIfAbsent(description, k -> new HashSet<>()).add(jar);
			}
		}
		return summariesInJars;
	}

	public List<IJarInfo> jars() {
		return jars;
	}

	public Optional<JsonElement> asJson() {
		return SerializerCodecs.SCAN_RESULT_CODEC.apply(jarScanner.config()).encodeStart(JsonOps.INSTANCE, this).result();
	}

	public boolean doesInheritFrom(String className, String parentClassName) {
		// Check if it's the same class
		if(className.equals(parentClassName)) {
			return true;
		}

		// Check direct and indirect parents
		Set<String> parents = classToParents.get(className);
		return parents != null && parents.contains(parentClassName);
	}

	@Override
	public Set<IClassInfo> classesWithType(String parentClassName) {
		if(!parentToChildrenInfo.containsKey(parentClassName)) {
			return Collections.emptySet();
		}

		return parentToChildrenInfo.get(parentClassName);
	}

	@Override
	public Set<String> supersOf(String className) {
		return classToParents.computeIfAbsent(className, k -> new HashSet<>());
	}
}
