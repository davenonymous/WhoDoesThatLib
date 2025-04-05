package com.davenonymous.whodoesthatlib.api.result;

import java.util.List;

/**
 * Represents information about a JAR file that contains mods.
 * This interface extends IJarInfo with functionality specific to JAR files
 * containing mod data.
 *
 * @param <T> The type of mod info contained in this JAR, extending IModInfo
 */
public interface IModdedJarInfo<T extends IModInfo> extends IJarInfo {
	/**
	 * Gets all mods contained in this JAR.
	 *
	 * @return List of mod information objects
	 */
	List<T> mods();
}
