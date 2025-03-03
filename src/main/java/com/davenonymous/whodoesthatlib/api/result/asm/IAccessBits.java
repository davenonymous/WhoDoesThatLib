package com.davenonymous.whodoesthatlib.api.result.asm;

public interface IAccessBits {
	boolean isPublic();

	boolean isProtected();

	boolean isPrivate();

	boolean isStatic();

	boolean isFinal();

	boolean isAbstract();

	boolean isInterface();
}
