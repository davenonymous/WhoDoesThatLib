package com.davenonymous.whodoesthatlib.impl.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

public class GsonHelper {

	public static JsonElement parse(String json) {
		return com.google.gson.JsonParser.parseString(json);
	}

	public static String toStableString(JsonElement json) {
		StringWriter stringwriter = new StringWriter();
		JsonWriter jsonwriter = new JsonWriter(stringwriter);
		jsonwriter.setIndent("  ");

		try {
			writeValue(jsonwriter, json, Comparator.naturalOrder());
		} catch (IOException ioexception) {
			throw new AssertionError(ioexception);
		}

		return stringwriter.toString();
	}

	public static void writeValue(JsonWriter writer, @Nullable JsonElement jsonElement, @Nullable Comparator<String> sorter) throws IOException {
		if(jsonElement == null || jsonElement.isJsonNull()) {
			writer.nullValue();
		} else if(jsonElement.isJsonPrimitive()) {
			JsonPrimitive jsonprimitive = jsonElement.getAsJsonPrimitive();
			if(jsonprimitive.isNumber()) {
				writer.value(jsonprimitive.getAsNumber());
			} else if(jsonprimitive.isBoolean()) {
				writer.value(jsonprimitive.getAsBoolean());
			} else {
				writer.value(jsonprimitive.getAsString());
			}
		} else if(jsonElement.isJsonArray()) {
			writer.beginArray();

			for(JsonElement jsonelement : jsonElement.getAsJsonArray()) {
				writeValue(writer, jsonelement, sorter);
			}

			writer.endArray();
		} else {
			if(!jsonElement.isJsonObject()) {
				throw new IllegalArgumentException("Couldn't write " + jsonElement.getClass());
			}

			writer.beginObject();

			for(Map.Entry<String, JsonElement> entry : sortByKeyIfNeeded(jsonElement.getAsJsonObject().entrySet(), sorter)) {
				writer.name(entry.getKey());
				writeValue(writer, entry.getValue(), sorter);
			}

			writer.endObject();
		}
	}

	private static Collection<Map.Entry<String, JsonElement>> sortByKeyIfNeeded(
		Collection<Map.Entry<String, JsonElement>> entries, @Nullable Comparator<String> sorter
	)
	{
		if(sorter == null) {
			return entries;
		} else {
			List<Map.Entry<String, JsonElement>> list = new ArrayList<>(entries);
			list.sort(Map.Entry.comparingByKey(sorter));
			return list;
		}
	}
}
