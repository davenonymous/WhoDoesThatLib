package com.davenonymous.whodoesthatlib.impl.jar;

import com.davenonymous.whodoesthatlib.api.IJarScanner;
import com.davenonymous.whodoesthatlib.api.analyzers.IJarAnalyzer;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.impl.GlobHelper;
import com.davenonymous.whodoesthatlib.impl.result.JarInfo;
import com.davenonymous.whodoesthatlib.impl.serialize.GsonHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;

public class MinecraftLocalizations implements IJarAnalyzer {
	@Override
	public void visitFileInJar(IJarScanner scanner, FileSystem jar, Path file, BasicFileAttributes attributes, IJarInfo result) {
		if(!(result instanceof JarInfo jarInfo)) {
			return;
		}

		String regExPattern = GlobHelper.toUnixRegexPattern("**/assets/*/lang/*.json");
		if(!file.toString().matches(regExPattern)) {
			return;
		}

		String fileName = file.getFileName().toString(); // e.g. en_us.json
		String langCode = fileName.substring(0, fileName.length() - 5).toLowerCase(Locale.ROOT); // e.g. en_us
		if(!scanner.config().isLanguageIncluded(langCode)) {
			return;
		}

		try {
			String jsonRaw = Files.readString(file);
			JsonElement json = GsonHelper.parse(jsonRaw);
			if(!(json instanceof JsonObject jsonObject)) {
				return;
			}

			for(var translationKey : jsonObject.keySet()) {
				String translation = jsonObject.get(translationKey).getAsString();
				jarInfo.addLocalization(translation);
			}

		} catch (IOException ignore) {
		}
	}
}
