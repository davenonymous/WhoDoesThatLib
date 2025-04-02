package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.UsedTypeDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.UsedTypeResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import java.util.*;

public class UsedTypesAnalyzer implements IModClassAnalyzer<UsedTypeResult> {
	private JsonObject jsonResult;
	private Map<Type, List<IClassInfo>> objResult;
	private List<UsedTypeDescription> descriptors;

	@Override
	public void onInit(IScanResult scanResult) {
		descriptors = scanResult.getSummaryDescriptions(UsedTypeDescription.ID);
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
	public UsedTypeResult result() {
		return new UsedTypeResult(objResult);
	}

	@Override
	public void visitClass(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IClassInfo classInfo) {
		for(UsedTypeDescription description : descriptors) {
			String typeClassName = description.getTypeClassName();
			Set<Type> matchingTypes = classInfo.usesType(typeClassName);

			if(!matchingTypes.isEmpty()) {
				if(!jsonResult.has(typeClassName)) {
					jsonResult.add(typeClassName, new JsonArray());
				}

				for(Type matchingType : matchingTypes) {
					jsonResult.getAsJsonArray(typeClassName).add(matchingType.getClassName());
					objResult.computeIfAbsent(matchingType, k -> new ArrayList<>()).add(classInfo);
				}

				jarInfo.addSummary(description, classInfo);
			}
		}

	}

	@Override
	public String getKey() {
		return UsedTypeDescription.ID;
	}
}
