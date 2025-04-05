package com.davenonymous.whodoesthatlib.api.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IModInfo;

import java.util.Map;
import java.util.Optional;

/**
 * Extends the base mod information interface with NeoForge-specific properties.
 * Provides access to additional information available in NeoForge mod metadata.
 */
public interface INeoForgeModInfo extends IModInfo {

	/**
	 * Gets acknowledgments and credits for the mod.
	 * @return An Optional containing credits text, or empty if not specified
	 */
	Optional<String> credits();

	/**
	 * Determines whether the mod logo should have a blur effect applied.
	 * @return An Optional containing true if blur should be applied, false otherwise, or empty if not specified
	 */
	Optional<Boolean> logoBlur();

	/**
	 * Gets the URL for update checking JSON.
	 * @return An Optional containing the update URL, or empty if not specified
	 */
	Optional<String> updateJSONURL();

	/**
	 * Gets additional custom properties defined in the mod metadata.
	 * @return An Optional containing a map of property names to values, or empty if none exist
	 */
	Optional<Map<String, Object>> customProperties();
}
