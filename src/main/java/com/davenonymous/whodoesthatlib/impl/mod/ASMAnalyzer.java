package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModAnalyzerWithValue;
import com.davenonymous.whodoesthatlib.api.descriptors.ISummaryDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.mod.IModResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ASMAnalyzer<ResultType extends IModResult, MapKey, MapValue, DescriptorType extends ISummaryDescription> implements IModAnalyzerWithValue<ResultType> {
	protected JsonObject jsonResult;
	protected Map<MapKey, List<MapValue>> objResult;
	protected List<DescriptorType> descriptors;

	@Override
	public void onInit(IScanResult scanResult) {
		descriptors = scanResult.getSummaryDescriptions(getKey());
	}

	@Override
	public void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
		jsonResult = new JsonObject();
		objResult = new HashMap<>();
	}

	@Override
	public JsonElement encodedResult() {
		if(jsonResult.isEmpty()) {
			return JsonNull.INSTANCE;
		}
		return jsonResult;
	}
}
