package com.davenonymous.whodoesthatlib.api.descriptors;

import com.davenonymous.whodoesthatlib.impl.serialize.StringyElementType;

import java.util.*;

public class AnnotationDescription extends AbstractSummaryDescription<AnnotationDescription> {
	public static final String ID = "annotations";
	private final String annotationClassName;
	private final StringyElementType elementType;
	private final Set<String> params;

	public AnnotationDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("annotation")) {
			this.annotationClassName = (String) configEntry.get("annotation");
		} else {
			this.annotationClassName = (String) configEntry.get("id");
		}

		if(configEntry.containsKey("type")) {
			this.elementType = StringyElementType.byKey(configEntry.get("type").toString());
		} else {
			this.elementType = StringyElementType.TYPE;
		}

		this.params = new HashSet<>();
		if(configEntry.containsKey("params")) {
			List<String> params = (List<String>) configEntry.get("params");
			this.params.addAll(params);
		}
	}

	public StringyElementType getElementType() {
		return elementType;
	}

	public String getAnnotationClassName() {
		return annotationClassName;
	}

	public List<String> getIncludedParams() {
		var result = new ArrayList<>(params);
		result.sort(Comparator.naturalOrder());
		return result;
	}

}
