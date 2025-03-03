package com.davenonymous.whodoesthatlib.impl.result.asm;

import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

public record FieldInfo(IClassInfo owner, Type type, String name, Set<IAnnotationInfo> annotationSet, Access access) implements IFieldInfo {
	public static FieldInfo of(IClassInfo owner, Type type, String name, int access) {
		return new FieldInfo(owner, type, name, new HashSet<>(), new Access(access));
	}

	public FieldInfo addAnnotation(IAnnotationInfo info) {
		this.annotationSet.add(info);
		return this;
	}

	@Override
	public Set<IAnnotationInfo> annotations() {
		return annotationSet;
	}

}
