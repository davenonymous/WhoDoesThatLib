package com.davenonymous.whodoesthatlib.impl.jar;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.fabric.FabricJarInfo;
import com.davenonymous.whodoesthatlib.impl.result.fabric.FabricModInfo;
import com.davenonymous.whodoesthatlib.api.GsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FabricMod implements IJarAnalyzer {
	@Override
	public void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result) {
		if(!file.toString().endsWith("/fabric.mod.json")) {
			return;
		}

		if(!(result instanceof FabricJarInfo jarInfo)) {
			return;
		}

		try {
			String jsonRaw = Files.readString(file);
			JsonElement json = GsonHelper.parse(jsonRaw);
			if(!(json instanceof JsonObject jsonObject)) {
				return;
			}

			if(!jsonObject.has("id")) {
				return;
			}

			jarInfo.addMod(FabricModInfo.fromJson(jsonObject));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
