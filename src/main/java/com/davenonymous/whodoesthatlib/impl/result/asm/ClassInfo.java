package com.davenonymous.whodoesthatlib.impl.result.asm;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClassInfo
	implements IClassInfo
{
	private IJarInfo owner;
	private Type type;
	private Type inherits;
	private Set<Type> interfaces;
	private Set<IMethodInfo> methods;
	private Set<IAnnotationInfo> annotations;
	private Set<IFieldInfo> fields;
	private Set<Type> usedTypes;
	private Set<String> calledMethods;
	private Access access;

	private final Map<String, Set<String>> calledMethodCache = new HashMap<>();
	private final Map<String, Set<Type>> usedTypeCache = new HashMap<>();

	public ClassInfo(
		IJarInfo owner,
		Type type, Type inherits,
		Set<Type> interfaces,
		Set<IMethodInfo> methods,
		Set<IAnnotationInfo> annotations,
		Set<IFieldInfo> fields,
		Set<Type> usedTypes,
		Set<String> calledMethods,
		int access)
	{
		this.owner = owner;
		this.type = type;
		this.inherits = inherits;
		this.interfaces = interfaces;
		this.methods = methods;
		this.annotations = annotations;
		this.fields = fields;
		this.access = new Access(access);
		this.usedTypes = usedTypes;
		this.calledMethods = calledMethods;
	}

	public static ClassInfo of(IJarInfo owner, Type asmType, Type asmSuperType, Set<Type> interfaces, int access) {
		return new ClassInfo(owner, asmType, asmSuperType, interfaces, new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), access);
	}

	public IJarInfo owner() {
		return owner;
	}

	@Override
	public Access access() {
		return access;
	}

	@Override
	public Type type() {
		return type;
	}

	@Override
	public Type inherits() {
		return inherits;
	}

	@Override
	public Set<Type> interfaces() {
		return interfaces;
	}

	@Override
	public Set<IMethodInfo> methods() {
		return methods;
	}

	@Override
	public Set<IAnnotationInfo> annotations() {
		return annotations;
	}

	@Override
	public Set<IFieldInfo> fields() {
		return fields;
	}

	@Override
	public Set<Type> usedTypes() {
		return usedTypes;
	}

	@Override
	public Set<String> calledMethods() {
		return calledMethods;
	}

	public ClassInfo setFields(Set<IFieldInfo> fields) {
		this.fields = fields;
		return this;
	}

	public ClassInfo setAnnotations(Set<IAnnotationInfo> annotations) {
		this.annotations = annotations;
		return this;
	}

	public ClassInfo setMethods(Set<IMethodInfo> methods) {
		this.methods = methods;
		return this;
	}

	public ClassInfo addCalledMethod(String method) {
		this.calledMethods.add(method);
		return this;
	}

	public ClassInfo addUsedType(Type usedType) {
		this.usedTypes.add(usedType);
		return this;
	}

	@Override
	public Set<String> calledMethods(String methodName) {
		if(!calledMethodCache.containsKey(methodName)) {
			Set<String> result = calledMethods.stream()
				.filter(method -> method.equals(methodName) || method.matches(methodName))
				.collect(Collectors.toSet());
			calledMethodCache.put(methodName, result);
		}
		return calledMethodCache.get(methodName);
	}

	@Override
	public Set<Type> usesType(String typeQuery) {
		if(!usedTypeCache.containsKey(typeQuery)) {
			Set<Type> result = usedTypes.stream()
				.filter(type -> type.getClassName().equals(typeQuery) || type.getClassName().matches(typeQuery))
				.collect(Collectors.toSet());
			usedTypeCache.put(typeQuery, result);
		}

		return usedTypeCache.get(typeQuery);
	}

	@Override
	public boolean inherits(IScanResult scanResult, String parentClassName) {
		return scanResult.doesInheritFrom(type().getClassName(), parentClassName);
	}
}
