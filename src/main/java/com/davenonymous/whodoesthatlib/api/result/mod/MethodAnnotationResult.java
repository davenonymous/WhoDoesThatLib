package com.davenonymous.whodoesthatlib.api.result.mod;

import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

public record MethodAnnotationResult(Map<Type, List<IMethodInfo>> methods) implements IModResult {
}
