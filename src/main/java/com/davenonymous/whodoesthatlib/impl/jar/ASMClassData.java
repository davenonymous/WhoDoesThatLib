package com.davenonymous.whodoesthatlib.impl.jar;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;
import com.davenonymous.whodoesthatlib.impl.result.asm.parsers.ClassFileParser;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class ASMClassData implements IJarAnalyzer {

	@Override
	public void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result) {
		if(!file.toString().endsWith(".class")) {
			return;
		}

		try(InputStream in = Files.newInputStream(file)) {
			ClassReader reader = new ClassReader(in);
			ClassFileParser parser = new ClassFileParser(result);
			reader.accept(parser, 0);
			if(result instanceof JarInfo jarInfoResult) {
				jarInfoResult.addClass(parser.result());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
