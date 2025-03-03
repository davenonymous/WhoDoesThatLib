package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Set;

public interface ISummaryDescription {
	String configKey();

	String resultCategory();

	String resultId();

	String description();

	Set<String> tags();
}
