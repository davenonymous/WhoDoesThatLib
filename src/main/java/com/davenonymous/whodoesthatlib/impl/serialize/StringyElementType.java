package com.davenonymous.whodoesthatlib.impl.serialize;

import com.mojang.serialization.Codec;

import java.lang.annotation.ElementType;
import java.util.Locale;

public enum StringyElementType {
	TYPE(0, "type"),
	FIELD(1, "field"),
	METHOD(2, "method"),
	PARAMETER(3, "parameter"),
	CONSTRUCTOR(4, "constructor"),
	LOCAL_VARIABLE(5, "local_variable"),
	ANNOTATION_TYPE(6, "annotation_type"),
	PACKAGE(7, "package"),
	TYPE_PARAMETER(8, "type_parameter"),
	TYPE_USE(9, "type_use"),
	MODULE(10, "module"),
	RECORD_COMPONENT(11, "record_component");

	private final int id;
	private final String key;

	StringyElementType(int id, String key) {
		this.id = id;
		this.key = key;
	}

	Codec<StringyElementType> CODEC = Codec.STRING.xmap(
		StringyElementType::byKey,
		StringyElementType::key
	);

	public static StringyElementType byKey(String key) {
		return switch(key.toLowerCase(Locale.ROOT)) {
			case "class", "type" -> TYPE;
			case "field" -> FIELD;
			case "method" -> METHOD;
			case "parameter" -> PARAMETER;
			case "constructor" -> CONSTRUCTOR;
			case "local_variable" -> LOCAL_VARIABLE;
			case "annotation_type" -> ANNOTATION_TYPE;
			case "package" -> PACKAGE;
			case "type_parameter" -> TYPE_PARAMETER;
			case "type_use" -> TYPE_USE;
			case "module" -> MODULE;
			case "record_component" -> RECORD_COMPONENT;
			default -> throw new IllegalArgumentException("Unknown key: " + key);
		};
	}

	public static StringyElementType byElementType(ElementType elementType) {
		return switch(elementType) {
			case TYPE -> TYPE;
			case FIELD -> FIELD;
			case METHOD -> METHOD;
			case PARAMETER -> PARAMETER;
			case CONSTRUCTOR -> CONSTRUCTOR;
			case LOCAL_VARIABLE -> LOCAL_VARIABLE;
			case ANNOTATION_TYPE -> ANNOTATION_TYPE;
			case PACKAGE -> PACKAGE;
			case TYPE_PARAMETER -> TYPE_PARAMETER;
			case TYPE_USE -> TYPE_USE;
			case MODULE -> MODULE;
			case RECORD_COMPONENT -> RECORD_COMPONENT;
		};
	}

	public ElementType getElementType() {
		return switch(this) {
			case TYPE -> ElementType.TYPE;
			case FIELD -> ElementType.FIELD;
			case METHOD -> ElementType.METHOD;
			case PARAMETER -> ElementType.PARAMETER;
			case CONSTRUCTOR -> ElementType.CONSTRUCTOR;
			case LOCAL_VARIABLE -> ElementType.LOCAL_VARIABLE;
			case ANNOTATION_TYPE -> ElementType.ANNOTATION_TYPE;
			case PACKAGE -> ElementType.PACKAGE;
			case TYPE_PARAMETER -> ElementType.TYPE_PARAMETER;
			case TYPE_USE -> ElementType.TYPE_USE;
			case MODULE -> ElementType.MODULE;
			case RECORD_COMPONENT -> ElementType.RECORD_COMPONENT;
		};
	}

	public int id() {
		return this.id;
	}

	public String key() {
		return key;
	}
}
