package com.davenonymous.whodoesthatlib.impl.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import org.tomlj.TomlTable;

import java.util.Locale;
import java.util.Optional;

public record NeoForgeDependencyInfo(
	String modId,
	boolean mandatory,
	Optional<String> versionRange) implements IDependencyInfo
{
	public static NeoForgeDependencyInfo fromToml(TomlTable depTable) {
		String depModId = depTable.getString("modId");
		String depType = depTable.getString("type", () -> "required").toLowerCase(Locale.ROOT);
		boolean mandatory = depTable.getBoolean("mandatory", () -> !depType.equals("optional"));
		String versionRange = depTable.getString("versionRange");

		return new NeoForgeDependencyInfo(
			depModId,
			mandatory,
			Optional.ofNullable(versionRange)
		);
	}

}
