package com.davenonymous.whodoesthatlib.impl.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeJarInfo;
import com.davenonymous.whodoesthatlib.api.result.neoforge.INeoForgeModInfo;
import com.davenonymous.whodoesthatlib.impl.LicenseHelper;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class NeoForgeJarInfo extends JarInfo implements INeoForgeJarInfo {
	String modLoader;
	String license;
	String issuesUrl;
	List<INeoForgeModInfo> mods;
	boolean isOpenSource = false;

	public NeoForgeJarInfo(Path jarPath) {
		super(jarPath);
	}

	@Override
	public Optional<String> issuesUrl() {
		return Optional.ofNullable(issuesUrl);
	}

	@Override
	public String license() {
		return license;
	}

	@Override
	public String modLoader() {
		return modLoader;
	}

	@Override
	public List<INeoForgeModInfo> mods() {
		return mods;
	}

	@Override
	public boolean isOpenSource() {
		return isOpenSource;
	}

	public NeoForgeJarInfo setIssuesUrl(String issuesUrl) {
		if(issuesUrl == null || issuesUrl.isBlank()) {
			this.issuesUrl = null;
			return this;
		}
		this.issuesUrl = issuesUrl;
		return this;
	}

	public NeoForgeJarInfo setLicense(String license) {
		if(license == null || license.isBlank()) {
			this.license = "All rights reserved";
			this.isOpenSource = false;
			return this;
		}

		this.license = license;

		boolean openSource = false;
		var detectedOpenSourceLicense = LicenseHelper.getLicenseFromString(license);
		if(detectedOpenSourceLicense.isPresent()) {
			this.license = detectedOpenSourceLicense.get();
			this.isOpenSource = true;
		}

		return this;
	}

	public NeoForgeJarInfo setModLoader(String modLoader) {
		if(modLoader == null || modLoader.isBlank()) {
			this.modLoader = "javafml";
			return this;
		}
		this.modLoader = modLoader;
		return this;
	}

	public NeoForgeJarInfo setMods(List<INeoForgeModInfo> mods) {
		this.mods = mods;
		return this;
	}

}
