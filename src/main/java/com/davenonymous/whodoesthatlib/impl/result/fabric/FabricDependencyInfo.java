package com.davenonymous.whodoesthatlib.impl.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import com.google.gson.JsonElement;

import java.util.Optional;

public record FabricDependencyInfo(
	String modId,
	boolean mandatory,
	Optional<String> versionRange) implements IDependencyInfo
{
	public static FabricDependencyInfo fromJson(JsonElement depTable) {
		return null;
	}

}
