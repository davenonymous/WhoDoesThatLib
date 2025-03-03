package com.davenonymous.whodoesthatlib.impl.result.asm;

import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo implements IMethodInfo {
	private IClassInfo owner;
	private String name;
	private Type returnType;
	private List<Type> parameters;
	private List<IAnnotationInfo> annotations;
	private Access access;

	public MethodInfo(IClassInfo owner, String name, Type methodType, int access) {
		this.owner = owner;
		this.name = name;
		this.returnType = methodType.getReturnType();
		this.parameters = List.of(methodType.getArgumentTypes());
		this.annotations = new ArrayList<>();
		this.access = new Access(access);
	}

	@Override
	public Access access() {
		return access;
	}

	@Override
	public IClassInfo owner() {
		return owner;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Type returnType() {
		return returnType;
	}

	@Override
	public List<Type> parameters() {
		return parameters;
	}

	@Override
	public List<IAnnotationInfo> annotations() {
		return annotations;
	}

	public MethodInfo addAnnotation(IAnnotationInfo info) {
		this.annotations.add(info);
		return this;
	}

}
