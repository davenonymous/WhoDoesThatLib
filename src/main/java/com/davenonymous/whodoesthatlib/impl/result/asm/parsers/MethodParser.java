package com.davenonymous.whodoesthatlib.impl.result.asm.parsers;

import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.impl.result.asm.MethodInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MethodParser extends MethodVisitor {
	private MethodInfo result;
	private Set<AnnotationParser> annotations;
	private Set<Type> usedTypes;
	private Set<String> calledMethods;

	public MethodParser(IClassInfo owner, String name, String desc, int access) {
		super(Opcodes.ASM9);
		this.result = new MethodInfo(owner, name, Type.getMethodType(desc), access);
		this.annotations = new HashSet<>();
		this.calledMethods = new HashSet<>();
		this.usedTypes = new HashSet<>();
		this.usedTypes.addAll(this.result.parameters());
		this.usedTypes.add(this.result.returnType());
	}

	public IMethodInfo getResult() {
		annotations.stream().map(AnnotationParser::getResult).forEach(info -> {
			result.addAnnotation(info);
		});
		return result;
	}

	public Set<Type> getUsedTypes() {
		return usedTypes;
	}

	public Set<String> getCalledMethods() {
		return calledMethods;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		var annotationParser = new AnnotationParser(ElementType.METHOD, descriptor);
		this.annotations.add(annotationParser);
		return annotationParser;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		Type ownerType = Type.getObjectType(owner);
		Type methodType = Type.getMethodType(descriptor);
		String methodName = ownerType.getClassName() + "." + name;

		usedTypes.addAll(Arrays.stream(methodType.getArgumentTypes()).toList());
		usedTypes.add(methodType.getReturnType());
		usedTypes.add(ownerType);
		calledMethods.add(methodName);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		usedTypes.add(Type.getObjectType(owner));
	}


}
