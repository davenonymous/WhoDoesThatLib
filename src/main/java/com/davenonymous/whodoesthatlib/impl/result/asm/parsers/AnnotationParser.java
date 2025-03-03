package com.davenonymous.whodoesthatlib.impl.result.asm.parsers;

import com.davenonymous.whodoesthatlib.impl.result.asm.AnnotationInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;

public class AnnotationParser extends AnnotationVisitor {
	private AnnotationInfo result;

	public AnnotationParser(ElementType targetType, String descriptor) {
		super(Opcodes.ASM9);
		this.result = AnnotationInfo.of(targetType, Type.getType(descriptor));
	}

	@Override
	public void visit(String name, Object value) {
		if(name == null) {
			name = "value";
		}
		if(value == null) {
			return;
		}

		this.result.addParameter(name, value);
	}

	// TODO: Add other annotation parameter types


	@Override
	public AnnotationVisitor visitArray(String name) {
		return this;
	}

	public AnnotationInfo getResult() {
		return this.result;
	}
}
