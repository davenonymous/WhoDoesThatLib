package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModAnalyzerWithValue;
import com.davenonymous.whodoesthatlib.api.descriptors.AnnotationDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AnnotationAnalyzer<T extends IModResult, U> implements IModAnalyzerWithValue<T> {
	protected JsonObject jsonResult;
	protected Map<Type, List<U>> eventResultBuilder;

	@Override
	public String getKey() {
		return AnnotationDescription.ID;
	}

	@Override
	public JsonElement encodedResult() {
		if(jsonResult.isEmpty()) {
			return JsonNull.INSTANCE;
		}
		return jsonResult;
	}

	@Override
	public void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
		jsonResult = new JsonObject();
		eventResultBuilder = new HashMap<>();
	}

	public abstract void visit(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, U info);
}
