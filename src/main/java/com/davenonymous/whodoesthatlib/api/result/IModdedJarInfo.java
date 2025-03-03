package com.davenonymous.whodoesthatlib.api.result;

import java.util.List;

public interface IModdedJarInfo<T extends IModInfo> extends IJarInfo {
	List<T> mods();
}
