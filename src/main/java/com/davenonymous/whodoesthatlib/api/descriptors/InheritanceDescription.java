package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class InheritanceDescription extends AbstractSummaryDescription<InheritanceDescription> {
	public static final String ID = "inheritance";
	private final String parentClassName;
	private final boolean allowAbstract;
	private final boolean allowInterfaces;

	public InheritanceDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("parent")) {
			this.parentClassName = (String) configEntry.get("parent");
		} else {
			this.parentClassName = (String) configEntry.get("id");
		}

		if(configEntry.containsKey("allowAbstract")) {
			this.allowAbstract = (boolean) configEntry.get("allowAbstract");
		} else {
			this.allowAbstract = false;
		}

		if(configEntry.containsKey("allowInterfaces")) {
			this.allowInterfaces = (boolean) configEntry.get("allowInterfaces");
		} else {
			this.allowInterfaces = false;
		}
	}

	public String getParentClassName() {
		return parentClassName;
	}

	public boolean allowsAbstractClasses() {
		return allowAbstract;
	}

	public boolean allowsInterfaces() {
		return allowInterfaces;
	}
}
