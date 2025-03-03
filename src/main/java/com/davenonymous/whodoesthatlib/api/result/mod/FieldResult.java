package com.davenonymous.whodoesthatlib.api.result.mod;

import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

public record FieldResult(Map<Type, List<IFieldInfo>> fields) implements IModResult {
}
