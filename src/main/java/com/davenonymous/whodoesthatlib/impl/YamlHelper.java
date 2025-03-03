package com.davenonymous.whodoesthatlib.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class YamlHelper {

	public static Object primitivesFromJson(JsonElement json) {
		if(json.isJsonPrimitive()) {
			return json.getAsString();
		} else if(json.isJsonNull()) {
			return null;
		} else if(json.isJsonArray()) {
			List<Object> list = new ArrayList<>();
			for(JsonElement element : json.getAsJsonArray()) {
				list.add(primitivesFromJson(element));
			}
			return list;
		} else if(json.isJsonObject()) {
			JsonObject obj = json.getAsJsonObject();
			Map<String, Object> map = new TreeMap<>();
			for(String key : obj.keySet()) {
				JsonElement value = obj.get(key);
				if(value.isJsonPrimitive()) {
					map.put(key, primitivesFromJson(value));
				}
			}
			for(String key : obj.keySet()) {
				JsonElement value = obj.get(key);
				if(!value.isJsonPrimitive()) {
					map.put(key, primitivesFromJson(value));
				}
			}
			return map;
		} else {
			throw new IllegalArgumentException("Invalid JSON element: " + json);
		}
	}
}
