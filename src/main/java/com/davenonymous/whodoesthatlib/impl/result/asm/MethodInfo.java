package com.davenonymous.whodoesthatlib.impl.result.asm;

import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

public record MethodInfo(
	IClassInfo owner,
	String name,
	Type returnType,
	List<Type> parameters,
	List<IAnnotationInfo> annotations,
	Access access
) implements IMethodInfo {
	public MethodInfo(IClassInfo owner, String name, Type methodType, int access) {
		this(
			owner,
			name,
			methodType.getReturnType(),
			List.of(methodType.getArgumentTypes()),
			new ArrayList<>(),
			new Access(access)
		);
	}

	public MethodInfo addAnnotation(IAnnotationInfo info) {
		this.annotations.add(info);
		return this;
	}
}
