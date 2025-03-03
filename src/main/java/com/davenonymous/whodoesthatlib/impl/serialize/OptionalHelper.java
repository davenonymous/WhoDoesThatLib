package com.davenonymous.whodoesthatlib.impl.serialize;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class OptionalHelper {
	public static <T extends Map<?, ?>> Optional<T> optionalOfMap(T map) {
		if(map == null || map.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(map);
	}

	public static <T extends Collection<?>> Optional<T> optionalOfCollection(T collection) {
		if(collection == null || collection.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(collection);
	}

	public static class Builder<T> {
		private T value;

		public Builder<T> value(T value) {
			this.value = value;
			return this;
		}

		public Optional<T> build() {
			return Optional.ofNullable(value);
		}
	}
}
