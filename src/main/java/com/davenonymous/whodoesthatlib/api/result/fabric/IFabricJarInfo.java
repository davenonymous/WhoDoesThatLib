package com.davenonymous.whodoesthatlib.api.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.IModdedJarInfo;

import java.util.List;

/**
 * Represents information about a JAR file that contains Fabric mods.
 * This interface extends IModdedJarInfo with functionality specific to
 * Fabric mod JAR files.
 */
public interface IFabricJarInfo extends IModdedJarInfo<IFabricModInfo> {
	/**
	 * Gets all Fabric mods contained in this JAR.
	 *
	 * @return List of Fabric mod information objects
	 */
	List<IFabricModInfo> mods();
}
