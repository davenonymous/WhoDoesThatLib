package com.davenonymous.whodoesthatlib.impl.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeModInfo;
import com.davenonymous.whodoesthatlib.impl.serialize.OptionalHelper;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record NeoForgeModInfo(
	String modId,
	String displayName,
	String description,
	String version,
	Optional<String> authors,
	Optional<String> credits,
	Optional<String> logoFile,
	Optional<Boolean> logoBlur,
	Optional<String> modUrl,
	Optional<String> updateJSONURL,
	Optional<Map<String, Object>> customProperties,
	List<IDependencyInfo> dependencies
) implements INeoForgeModInfo
{
	public static NeoForgeModInfo fromToml(TomlTable modTable, List<IDependencyInfo> modDependencies, Map<String, Object> customProperties, TomlParseResult toml) {
		String modId = modTable.getString("modId", () -> "");
		String displayName = modTable.getString("displayName", () -> "");
		String description = modTable.getString("description", () -> "");
		String version = modTable.getString("version", () -> "");
		Optional<String> authors;
		if(modTable.isArray("authors")) {
			TomlArray authorsArray = modTable.getArray("authors");
			String joinedAuthors = authorsArray.toList().stream().map(Object::toString).collect(Collectors.joining(", "));
			authors = Optional.of(joinedAuthors);
		} else {
			authors = Optional.ofNullable(modTable.getString("authors"));
		}


		Optional<Map<String, Object>> finalCustomProperties = OptionalHelper.optionalOfMap(customProperties);
		Optional<String> credits = Optional.ofNullable(modTable.getString("credits"));
		Optional<String> logoFile = Optional.ofNullable(modTable.getString("logoFile"));
		Optional<Boolean> logoBlur = Optional.ofNullable(modTable.getBoolean("logoBlur"));
		Optional<String> modUrl = Optional.ofNullable(modTable.getString("modUrl"));
		Optional<String> updateJSONURL = Optional.ofNullable(modTable.getString("updateJSONURL"));

		if(toml.contains("modLogo") && logoFile.isEmpty()) {
			String modLogo = toml.getString("modLogo");
			if(modLogo != null) {
				logoFile = Optional.of(modLogo);
			}
		}

		return new NeoForgeModInfo(
			modId,
			displayName,
			description,
			version,
			authors,
			credits,
			logoFile,
			logoBlur,
			modUrl,
			updateJSONURL,
			finalCustomProperties,
			modDependencies
		);
	}

}
