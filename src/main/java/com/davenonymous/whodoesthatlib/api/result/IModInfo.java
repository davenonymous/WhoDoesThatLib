package com.davenonymous.whodoesthatlib.api.result;

import java.util.List;
import java.util.Optional;

/**
 * Base interface for mod information.
 * Provides access to common properties that all mods have regardless of mod loader.
 */
public interface IModInfo {
	/**
	 * Gets the unique identifier for the mod.
	 * @return The mod identifier string
	 */
	String modId();

	/**
	 * Gets the user-friendly name of the mod.
	 * @return The display name of the mod
	 */
	String displayName();

	/**
	 * Gets a brief description of the mod's functionality.
	 * @return The mod description
	 */
	String description();

	/**
	 * Gets the version string of the mod.
	 * @return The version of the mod
	 */
	String version();

	/**
	 * Gets the list of authors who created the mod.
	 * @return An Optional containing the authors as a string, or empty if not specified
	 */
	Optional<String> authors();

	/**
	 * Gets the path to the mod's logo file.
	 * @return An Optional containing the logo file path, or empty if no logo is specified
	 */
	Optional<String> logoFile();

	/**
	 * Gets the URL to the mod's website or repository.
	 * @return An Optional containing the mod's URL, or empty if not specified
	 */
	Optional<String> modUrl();

	/**
	 * Gets a list of the mod's dependencies.
	 * @return A List of dependency information objects
	 */
	List<IDependencyInfo> dependencies();
}
