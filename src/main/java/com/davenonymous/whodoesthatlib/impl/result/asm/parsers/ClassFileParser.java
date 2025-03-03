package com.davenonymous.whodoesthatlib.impl.result.asm.parsers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.impl.result.asm.ClassInfo;
import org.objectweb.asm.*;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClassFileParser extends ClassVisitor {
	private IJarInfo owner;
	private Type asmType;
	private Type asmSuperType;
	private Set<Type> interfaces;
	private Set<MethodParser> methods;
	private Set<AnnotationParser> annotations;
	private Set<FieldParser> fields;

	private ClassInfo result;

	public ClassFileParser(IJarInfo owner) {
		super(Opcodes.ASM9);
		this.owner = owner;
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		// Main.logger.info("{} extends {} [", name, superName);
		this.asmType = Type.getObjectType(name);
		this.asmSuperType = superName != null && !superName.isEmpty() ? Type.getObjectType(superName) : null;
		this.interfaces = Stream.of(interfaces).map(Type::getObjectType).collect(Collectors.toSet());
		this.methods = new HashSet<>();
		this.annotations = new HashSet<>();
		this.fields = new HashSet<>();

		this.result = ClassInfo.of(owner, asmType, asmSuperType, this.interfaces, access);
		this.result.addUsedType(asmType);
		if(asmSuperType != null) {
			this.result.addUsedType(asmSuperType);
		}
		this.interfaces.forEach(this.result::addUsedType);
	}

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		var fieldParser = new FieldParser(result, name, desc, access);
		this.fields.add(fieldParser);
		return fieldParser;
	}

	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		var methodParser = new MethodParser(result, name, desc, access);
		this.methods.add(methodParser);
		return methodParser;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		var annotationParser = new AnnotationParser(ElementType.TYPE, descriptor);
		this.annotations.add(annotationParser);
		return annotationParser;
	}

	public IClassInfo result() {
		result.setAnnotations(annotations.stream().map(AnnotationParser::getResult).collect(Collectors.toSet()));
		result.setFields(fields.stream().map(FieldParser::getResult).collect(Collectors.toSet()));
		result.setMethods(methods.stream().map(MethodParser::getResult).collect(Collectors.toSet()));

		fields.stream().map(FieldParser::getResult).map(IFieldInfo::type).forEach(usedType -> {
			result.addUsedType(usedType);
		});
		methods.stream().flatMap(method -> method.getUsedTypes().stream()).forEach(usedType -> {
			result.addUsedType(usedType);
		});
		methods.stream().flatMap(method -> method.getCalledMethods().stream()).forEach(calledMethod -> {
			result.addCalledMethod(calledMethod);
		});

		return result;
	}

}
