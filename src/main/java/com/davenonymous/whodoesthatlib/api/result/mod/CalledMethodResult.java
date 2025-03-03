package com.davenonymous.whodoesthatlib.api.result.mod;

import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;

import java.util.List;
import java.util.Map;

public record CalledMethodResult(Map<String, List<IClassInfo>> methodUsage) implements IModResult {
}
