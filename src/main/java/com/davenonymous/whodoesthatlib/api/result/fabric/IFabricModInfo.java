package com.davenonymous.whodoesthatlib.api.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.IModInfo;

import java.util.Optional;

public interface IFabricModInfo extends IModInfo {
	String license();

	boolean isOpenSource();

	Optional<String> sourcesURL();
}
