package com.davenonymous.whodoesthatlib.api.result;

import java.util.Optional;

/**
 * Represents information about a mod dependency.
 * <p>
 * This interface provides a common structure for dependency information
 * across different mod loaders (like Fabric and NeoForge). It encapsulates
 * the essential details about mod dependencies: the mod ID, whether the
 * dependency is mandatory, and any version constraints.
 * <p>
 * Implementations of this interface exist for different mod platforms,
 * such as {@code FabricDependencyInfo} and {@code NeoForgeDependencyInfo}.
 */
public interface IDependencyInfo {
	/**
	 * Gets the mod ID of the dependency.
	 *
	 * @return The string identifier of the required or suggested mod
	 */
	String modId();

	/**
	 * Determines whether this dependency is mandatory.
	 * <p>
	 * A mandatory dependency ({@code true}) means the mod requires this dependency
	 * to function properly. A non-mandatory dependency ({@code false}) is considered
	 * optional or suggested.
	 *
	 * @return {@code true} if the dependency is required, {@code false} if it's optional
	 */
	boolean mandatory();

	/**
	 * Gets the version range constraints for this dependency, if specified.
	 *
	 * @return An Optional containing the version range string, or an empty Optional
	 *         if no specific version is required
	 */
	Optional<String> versionRange();
}
