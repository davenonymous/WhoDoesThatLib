package com.davenonymous.whodoesthatlib.impl.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.IDependencyInfo;
import com.davenonymous.whodoesthatlib.api.result.fabric.IFabricModInfo;
import com.davenonymous.whodoesthatlib.impl.LicenseHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public record FabricModInfo(
	String modId,
	String displayName,
	String description,
	String version,
	String license,
	boolean isOpenSource,
	Optional<String> authors,
	Optional<String> logoFile,
	Optional<String> modUrl,
	Optional<String> sourcesURL,
	List<IDependencyInfo> dependencies
) implements IFabricModInfo
{
	public static FabricModInfo fromJson(JsonObject root) {
		String modId = root.get("id").getAsString();
		String displayName = root.get("name").getAsString();
		String description = root.get("description").getAsString();
		String version = root.get("version").getAsString();
		String license = root.get("license").getAsString();

		boolean isOpenSource = false;
		var detectedOpenSourceLicense = LicenseHelper.getLicenseFromString(license);
		if(detectedOpenSourceLicense.isPresent()) {
			license = detectedOpenSourceLicense.get();
			isOpenSource = true;
		}

		Optional<String> authors = Optional.empty();
		if(root.has("authors") && root.get("authors").isJsonArray()) {
			JsonArray authorsElement = root.get("authors").getAsJsonArray();
			authors = Optional.of(authorsElement.asList().stream().map(JsonElement::getAsString).sorted(Comparator.naturalOrder()).collect(Collectors.joining(", ")));
		}

		Optional<String> logoFile = Optional.empty();
		if(root.has("icon") && root.get("icon").isJsonPrimitive()) {
			logoFile = Optional.of(root.get("icon").getAsString());
		}
		Optional<String> modUrl = Optional.empty();
		Optional<String> sourcesURL = Optional.empty();
		if(root.has("contact") && root.get("contact").isJsonObject()) {
			JsonObject contact = root.get("contact").getAsJsonObject();
			if(contact.has("homepage") && contact.get("homepage").isJsonPrimitive()) {
				modUrl = Optional.of(contact.get("homepage").getAsString());
			}
			if(contact.has("sources") && contact.get("sources").isJsonPrimitive()) {
				sourcesURL = Optional.of(contact.get("sources").getAsString());
			}
		}

		List<IDependencyInfo> dependencies = new ArrayList<>();
		if(root.has("depends") && root.get("depends").isJsonObject()) {
			JsonObject depends = root.get("depends").getAsJsonObject();
			for(String depId : depends.keySet()) {
				JsonElement depVersion = depends.get(depId);
				if(depVersion.isJsonPrimitive()) {
					String depVersionString = depVersion.getAsString();
					dependencies.add(new FabricDependencyInfo(depId, true, depVersionString.equals("*") ? Optional.empty() : Optional.of(depVersionString)));
				}
			}
		}

		if(root.has("suggests") && root.get("suggests").isJsonObject()) {
			JsonObject suggests = root.get("suggests").getAsJsonObject();
			for(String depId : suggests.keySet()) {
				JsonElement depVersion = suggests.get(depId);
				if(depVersion.isJsonPrimitive()) {
					String depVersionString = depVersion.getAsString();
					dependencies.add(new FabricDependencyInfo(depId, false, depVersionString.equals("*") ? Optional.empty() : Optional.of(depVersionString)));
				}
			}
		}


		return new FabricModInfo(
			modId,
			displayName,
			description,
			version,
			license,
			isOpenSource,
			authors,
			logoFile,
			modUrl,
			sourcesURL,
			dependencies
		);
	}

}
