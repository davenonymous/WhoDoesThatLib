package com.davenonymous.whodoesthatlib.impl.result.fabric;

import com.davenonymous.whodoesthatlib.api.result.fabric.IFabricJarInfo;
import com.davenonymous.whodoesthatlib.api.result.fabric.IFabricModInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FabricJarInfo extends JarInfo implements IFabricJarInfo {
	List<IFabricModInfo> mods;

	public FabricJarInfo(Path jarPath) {
		super(jarPath);
		this.mods = new ArrayList<>();
	}

	@Override
	public List<IFabricModInfo> mods() {
		return mods;
	}

	public FabricJarInfo setMods(List<IFabricModInfo> mods) {
		this.mods = mods;
		return this;
	}

	public FabricJarInfo addMod(IFabricModInfo mod) {
		this.mods.add(mod);
		return this;
	}
}
