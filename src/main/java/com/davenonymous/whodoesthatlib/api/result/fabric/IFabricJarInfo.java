package com.davenonymous.whodoesthatlib.api.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.IModdedJarInfo;

import java.util.List;

public interface IFabricJarInfo extends IModdedJarInfo<IFabricModInfo> {
	List<IFabricModInfo> mods();
}
