package com.davenonymous.whodoesthatlib.api.analyzers;

import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;

import javax.annotation.Nullable;
import java.nio.file.Path;

public interface IModFileAnalyzer extends IModAnalyzer {
	void visitFile(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, Path file);
}
