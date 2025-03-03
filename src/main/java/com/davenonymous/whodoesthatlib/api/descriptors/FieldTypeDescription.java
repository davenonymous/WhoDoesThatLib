package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class FieldTypeDescription extends AbstractSummaryDescription<FieldTypeDescription> {
	public static final String ID = "fields";
	private final String fieldTypeName;

	public FieldTypeDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("type")) {
			this.fieldTypeName = (String) configEntry.get("type");
		} else {
			this.fieldTypeName = (String) configEntry.get("id");
		}
	}

	public String getFieldClassName() {
		return fieldTypeName;
	}

}
