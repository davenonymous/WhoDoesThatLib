package com.davenonymous.whodoesthatlib.api.result.mod;

import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import org.objectweb.asm.Type;

import java.util.List;
import java.util.Map;

public record MixinResult(Map<Type, List<IClassInfo>> mixins) implements IModResult {
}
