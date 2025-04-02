package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.CalledMethodDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.CalledMethodResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.*;

public class CalledMethodsAnalyzer implements IModClassAnalyzer<CalledMethodResult> {
	private JsonObject jsonResult;
	private Map<String, List<IClassInfo>> objResult;
	private List<CalledMethodDescription> descriptors;

	@Override
	public void onInit(IScanResult scanResult) {
		descriptors = scanResult.getSummaryDescriptions(CalledMethodDescription.ID);
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

	@Override
	public CalledMethodResult result() {
		return new CalledMethodResult(objResult);
	}

	@Override
	public void visitClass(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IClassInfo classInfo) {
		for(CalledMethodDescription description : descriptors) {
			String methodQuery = description.getMethodQuery();
			Set<String> matchingCalls = classInfo.calledMethods(methodQuery);

			if(!matchingCalls.isEmpty()) {
				if(!jsonResult.has(methodQuery)) {
					jsonResult.add(methodQuery, new JsonArray());
				}

				for(String matchingCall : matchingCalls) {
					jsonResult.getAsJsonArray(methodQuery).add(classInfo.getClassName());
					objResult.computeIfAbsent(matchingCall, k -> new ArrayList<>()).add(classInfo);
				}

				jarInfo.addSummary(description, classInfo);
			}
		}

	}

	@Override
	public String getKey() {
		return CalledMethodDescription.ID;
	}
}
