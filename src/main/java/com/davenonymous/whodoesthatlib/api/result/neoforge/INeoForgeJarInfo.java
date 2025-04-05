package com.davenonymous.whodoesthatlib.api.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IModdedJarInfo;

import java.util.Optional;

/**
 * Represents information about a JAR file that contains NeoForge mods.
 * This interface extends IModdedJarInfo with functionality specific to
 * NeoForge mod JAR files, including license information and issue tracking.
 */
public interface INeoForgeJarInfo extends IModdedJarInfo<INeoForgeModInfo> {
	/**
	 * Gets the mod loader type used by mods in this JAR.
	 *
	 * @return The mod loader identifier (typically "javafml")
	 */
	String modLoader();

	/**
	 * Gets the license under which the mod is distributed.
	 *
	 * @return The license name or description
	 */
	String license();

	/**
	 * Determines if the mod is open source based on its license.
	 *
	 * @return true if the mod has an open source license, false otherwise
	 */
	boolean isOpenSource();

	/**
	 * Gets the URL to the issue tracker for the mod.
	 *
	 * @return Optional URL to the issue tracker
	 */
	Optional<String> issuesUrl();
}
