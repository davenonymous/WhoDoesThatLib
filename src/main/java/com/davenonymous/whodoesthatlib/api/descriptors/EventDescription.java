package com.davenonymous.whodoesthatlib.api.descriptors;

import java.util.Map;

public class EventDescription extends AbstractSummaryDescription<EventDescription> {
	public static final String ID = "events";
	private final String eventClassName;

	public EventDescription(String configKey, Map<String, Object> configEntry) {
		super(configKey, configEntry);

		if(configEntry.containsKey("event")) {
			this.eventClassName = (String) configEntry.get("event");
		} else {
			this.eventClassName = (String) configEntry.get("id");
		}
	}

	public String getEventClassName() {
		return eventClassName;
	}

}
