package com.davenonymous.whodoesthatlib.impl.result.asm.parsers;

import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.impl.result.asm.FieldInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Set;

public class FieldParser extends FieldVisitor {
	private FieldInfo result;
	private Set<AnnotationParser> annotations;

	public FieldParser(IClassInfo owner, String name, String desc, int access) {
		super(Opcodes.ASM9);
		this.result = FieldInfo.of(owner, Type.getType(desc), name, access);
		this.annotations = new HashSet<>();
	}

	@Override
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		var annotationParser = new AnnotationParser(ElementType.FIELD, descriptor);
		this.annotations.add(annotationParser);
		return annotationParser;
	}

	public IFieldInfo getResult() {
		annotations.stream().map(AnnotationParser::getResult).forEach(info -> {
			result.addAnnotation(info);
		});
		return result;
	}
}
