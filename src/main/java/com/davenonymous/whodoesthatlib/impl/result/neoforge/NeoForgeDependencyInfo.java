package com.davenonymous.whodoesthatlib.impl.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import org.tomlj.TomlTable;

import java.util.Optional;

public record NeoForgeDependencyInfo(
	String modId,
	boolean mandatory,
	Optional<String> versionRange) implements IDependencyInfo
{
	public static NeoForgeDependencyInfo fromToml(TomlTable depTable) {
		String depModId = depTable.getString("modId");
		boolean mandatory = depTable.getBoolean("mandatory", () -> true);
		String versionRange = depTable.getString("versionRange");

		return new NeoForgeDependencyInfo(
			depModId,
			mandatory,
			Optional.ofNullable(versionRange)
		);
	}

}
