package com.davenonymous.whodoesthatlib.api.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IModdedJarInfo;

import java.util.Optional;

public interface INeoForgeJarInfo extends IModdedJarInfo<INeoForgeModInfo> {
	String modLoader();

	String license();

	boolean isOpenSource();

	Optional<String> issuesUrl();
}
