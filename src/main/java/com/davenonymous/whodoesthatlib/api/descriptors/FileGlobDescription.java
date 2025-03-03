package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class FileGlobDescription extends AbstractSummaryDescription<FileGlobDescription> {
	public static final String ID = "files";
	private final String glob;

	public FileGlobDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);
		this.glob = (String) configEntry.get("glob");
	}

	public String getPathGlob() {
		return glob;
	}

}
