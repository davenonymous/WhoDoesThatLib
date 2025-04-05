package com.davenonymous.whodoesthatlib.api.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.IModInfo;

import java.util.Optional;

/**
 * Extends the base mod information interface with Fabric-specific properties.
 * Provides access to additional information available in Fabric mod metadata.
 */
public interface IFabricModInfo extends IModInfo {
	/**
	 * Gets the license under which the mod is distributed.
	 * @return The license identifier or text
	 */
	String license();

	/**
	 * Determines whether the mod is open source.
	 * @return true if the mod is open source, false otherwise
	 */
	boolean isOpenSource();

	/**
	 * Gets the URL to the mod's source code repository.
	 * @return An Optional containing the sources URL, or empty if not specified
	 */
	Optional<String> sourcesURL();
}
