package com.davenonymous.whodoesthatlib.impl.result.asm;

import com.davenonymous.whodoesthatlib.api.result.asm.IAccessBits;
import org.objectweb.asm.Opcodes;

public record Access(int access) implements IAccessBits {
	public boolean isPublic() {
		return (access & Opcodes.ACC_PUBLIC) != 0;
	}

	public boolean isProtected() {
		return (access & Opcodes.ACC_PROTECTED) != 0;
	}

	public boolean isPrivate() {
		return (access & Opcodes.ACC_PRIVATE) != 0;
	}

	public boolean isStatic() {
		return (access & Opcodes.ACC_STATIC) != 0;
	}

	public boolean isFinal() {
		return (access & Opcodes.ACC_FINAL) != 0;
	}

	public boolean isAbstract() {
		return (access & Opcodes.ACC_ABSTRACT) != 0;
	}

	public boolean isInterface() {
		return (access & Opcodes.ACC_INTERFACE) != 0;
	}
}
