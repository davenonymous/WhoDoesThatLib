package com.davenonymous.whodoesthatlib.impl.serialize;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import org.objectweb.asm.Type;

import java.nio.file.Path;
import java.util.*;

public class Codecs {

	public static final Codec<JsonElement> JSON = converter(JsonOps.INSTANCE);
	public static Codec<Path> PATH_CODEC = Codec.STRING.xmap(
		Path::of,
		Path::toString
	);
	public static Codec<Path> PATH_FILENAME_CODEC = Codec.STRING.xmap(
		Path::of,
		p -> p.getFileName().toString()
	);
	public static Codec<Object> OBJECT_VALUE_CODEC = Codec.STRING.xmap(
		s -> new Object(),
		Object::toString
	);
	public static Codec<Type> TYPE_CODEC = Codec.STRING.xmap(
		Type::getType,
		Type::getClassName
	);

	public static <T> Codec<T> converter(DynamicOps<T> ops) {
		return Codec.PASSTHROUGH.xmap(p_304323_ -> p_304323_.convert(ops).getValue(), p_304327_ -> new Dynamic<>(ops, (T) p_304327_));
	}

	static <T extends Collection<?>> Optional<T> disabler(T collection, boolean enabled) {
		return enabled ? OptionalHelper.optionalOfCollection(collection) : Optional.empty();
	}

	static <T> Optional<T> disabler(Optional<T> value, boolean enabled) {
		return enabled ? value : Optional.empty();
	}

	private static <T> Optional<T> disabler(T value, boolean enabled) {
		return enabled ? Optional.of(value) : Optional.empty();
	}

	private static <T> List<T> setToSortedList(Set<T> set, Comparator<T> comparator) {
		List<T> list = new ArrayList<>(set);
		list.sort(comparator);
		return list;
	}

	static <K, T> Map<K, List<T>> mapSetToMapList(Map<K, Set<T>> map, Comparator<T> comparator) {
		Map<K, List<T>> result = new HashMap<>();
		for(var entry : map.keySet()) {
			result.computeIfAbsent(entry, k -> setToSortedList(map.get(k), comparator));
		}
		return result;
	}
}
