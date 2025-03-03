package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;

import javax.annotation.Nullable;

public interface IModClassAnalyzer<T extends IModResult> extends IModAnalyzerWithValue<T> {

	void visitClass(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, IClassInfo classInfo);
}
