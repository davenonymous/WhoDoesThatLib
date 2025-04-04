package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModMethodAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.EventDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.EventResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import java.util.*;

public class EventListenerAnalyzer extends ASMAnalyzer<EventResult, Type, IMethodInfo, EventDescription> implements IModMethodAnalyzer<EventResult> {
	private static final Set<String> validAnnotations = Set.of(
		"net.neoforged.bus.api.SubscribeEvent",
		"net.minecraftforge.eventbus.api.SubscribeEvent"
	);

	@Override
	public String getKey() {
		return EventDescription.ID;
	}

	@Override
	public EventResult result() {
		return new EventResult(objResult);
	}

	@Override
	public void visitMethod(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IClassInfo classInfo, IMethodInfo methodInfo) {
		for(var annotation : methodInfo.annotations()) {
			if(validAnnotations.contains(annotation.type().getClassName())) {
				Type eventClass = methodInfo.parameters().getFirst();
				String eventClassName = eventClass.getClassName();
				if(!jsonResult.has(eventClassName)) {
					jsonResult.add(eventClassName, new JsonArray());
				}
				objResult.computeIfAbsent(eventClass, k -> new ArrayList<>()).add(methodInfo);
				jsonResult.getAsJsonArray(eventClassName).add(classInfo.type().getClassName() + "#" + methodInfo.name());

				for(EventDescription description : descriptors) {
					if(description.getEventClassName().equals(eventClassName)) {
						jarInfo.addSummary(description, methodInfo);
					} else {
						// Try with older forge event class names
						String oldEventClassName = eventClassName.replace("net.minecraftforge", "net.neoforged.neoforge");
						if(description.getEventClassName().equals(oldEventClassName)) {
							jarInfo.addSummary(description, methodInfo);
						} else {
							// Try matching the last two parts of the class name
							String[] eventParts = eventClassName.split("\\.");
							String[] descParts = description.getEventClassName().split("\\.");
							if(eventParts.length >= 2 && descParts.length >= 2) {
								if(eventParts[eventParts.length - 1].equals(descParts[descParts.length - 1]) && eventParts[eventParts.length - 2].equals(
									descParts[descParts.length - 2])) {
									jarInfo.addSummary(description, methodInfo);
								}
							}
						}
					}
				}

				break;
			}
		}
	}
}
