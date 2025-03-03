package com.davenonymous.whodoesthatlib.api.result.mod;

import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

public record EventResult(Map<Type, List<IMethodInfo>> listeners) implements IModResult {
}
