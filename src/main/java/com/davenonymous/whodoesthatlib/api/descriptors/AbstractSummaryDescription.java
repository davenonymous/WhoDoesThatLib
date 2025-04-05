package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.*;

/**
 * Base implementation of {@link ISummaryDescription} that provides common functionality
 * for all descriptor types.
 * <p>
 * This abstract class reads configuration from a Map structure and provides methods
 * to manage tags and access description properties. It uses a self-referencing generic
 * type to enable fluent method chaining in subclasses.
 * <p>
 * Subclasses should extend this class to implement specific descriptor behaviors for
 * different code elements like annotations, method calls, inheritance relationships, etc.
 *
 * @param <T> Self-referencing type parameter for method chaining in subclasses
 */
public abstract class AbstractSummaryDescription<T extends AbstractSummaryDescription<?>> implements ISummaryDescription {
	/** The unique key identifying this description in configuration */
	String configKey;
	/** The category of the code element this description represents */
	String resultCategory;
	/** The identifier of this particular code element */
	String resultId;
	/** Human-readable description of what this element represents */
	String description;
	/** Set of tags associated with this description */
	Set<String> tags;

	/**
	 * Creates a new description from a configuration entry.
	 *
	 * @param configKey The unique key identifying this description in configuration
	 * @param configEntry The map containing the configuration data for this description
	 */
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

	/**
	 * Adds a collection of tags to this description.
	 *
	 * @param tags The collection of tags to add
	 * @return This instance for method chaining
	 */
	public T withTags(Collection<String> tags) {
		this.tags.addAll(tags);
		return (T) this;
	}

	/**
	 * Adds one or more tags to this description.
	 *
	 * @param tag The tags to add
	 * @return This instance for method chaining
	 */
	public T withTags(String... tag) {
		Collections.addAll(this.tags, tag);
		return (T) this;
	}

	/**
	 * Helper method to get a value from a configuration entry that might be either
	 * a single item or a list of items.
	 *
	 * @param key The key to look up in the config entry
	 * @param configEntry The configuration entry map
	 * @param <K> The type of the value(s) to retrieve
	 * @return A list containing either the single value or all values from the list,
	 *         or an empty list if the key doesn't exist
	 */
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
