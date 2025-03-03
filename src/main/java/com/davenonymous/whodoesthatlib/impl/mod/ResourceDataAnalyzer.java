package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModFileAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.FileGlobDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.impl.GlobHelper;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ResourceDataAnalyzer implements IModFileAnalyzer {
	private Map<String, Pattern> compiledGlobs;
	private List<FileGlobDescription> descriptors;

	private String prefixUnlessExists(String prefix, String path) {
		if(path.startsWith(prefix)) {
			return path;
		}
		return prefix + path;
	}

	@Override
	public void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
		this.descriptors = scanResult.getSummaryDescriptions(FileGlobDescription.ID);
		this.compiledGlobs = descriptors.stream().collect(Collectors.toMap(
			FileGlobDescription::getPathGlob,
			g -> Pattern.compile(GlobHelper.toUnixRegexPattern(
				prefixUnlessExists("/", g.getPathGlob()))
			)
		));
	}

	@Override
	public void visitFile(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, Path file) {

		for(FileGlobDescription descriptor : descriptors) {
			Predicate<String> matcher = compiledGlobs.get(descriptor.getPathGlob()).asMatchPredicate();
			if(matcher.test(file.toString())) {
				jarInfo.addSummary(descriptor, file);
			}
		}
	}

	@Override
	public String getKey() {
		return "";
	}
}
