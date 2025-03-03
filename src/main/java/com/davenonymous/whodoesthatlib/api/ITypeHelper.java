package com.davenonymous.whodoesthatlib.api;

public interface ITypeHelper {
	static String getSimpleClassName(String className) {
		var parts = className.split("\\.");
		return parts[parts.length - 1];
	}
}
