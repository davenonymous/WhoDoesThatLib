package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.*;

public abstract class AbstractSummaryDescription<T extends AbstractSummaryDescription<?>> implements ISummaryDescription {
	String configKey;
	String resultCategory;
	String resultId;
	String description;

	Set<String> tags;

	public AbstractSummaryDescription(String configKey, Map<String, Object> configEntry) {
		this.configKey = configKey;
		this.resultCategory = (String) configEntry.get("category");
		this.resultId = (String) configEntry.get("id");
		this.description = (String) configEntry.get("description");
		this.tags = new HashSet<>();
		if(configEntry.containsKey("tags")) {
			List<String> tags = (List<String>) configEntry.get("tags");
			this.tags.addAll(tags);
		}
	}

	public T withTags(Collection<String> tags) {
		this.tags.addAll(tags);
		return (T) this;
	}

	public T withTags(String... tag) {
		Collections.addAll(this.tags, tag);
		return (T) this;
	}

	protected <K> List<K> getSingleOrList(String key, Map<String, Object> configEntry) {
		if(configEntry.containsKey(key)) {
			var object = configEntry.get(key);
			if(object instanceof List) {
				//noinspection unchecked
				return (List<K>) object;
			} else {
				//noinspection unchecked
				return List.of((K) object);
			}
		}
		return List.of();
	}


	@Override
	public String configKey() {
		return configKey;
	}

	@Override
	public String resultCategory() {
		return resultCategory;
	}

	@Override
	public String resultId() {
		return resultId;
	}

	@Override
	public Set<String> tags() {
		return tags;
	}

	@Override
	public String description() {
		return description;
	}
}
