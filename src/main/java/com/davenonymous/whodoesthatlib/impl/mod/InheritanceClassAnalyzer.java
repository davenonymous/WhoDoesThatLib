package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.InheritanceDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.InheritanceResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InheritanceClassAnalyzer implements IModClassAnalyzer<InheritanceResult> {
	private JsonObject jsonResult;
	private Map<Type, List<IClassInfo>> objResult;
	private List<InheritanceDescription> descriptors;

	@Override
	public void onInit(IScanResult scanResult) {
		descriptors = scanResult.getSummaryDescriptions(InheritanceDescription.ID);
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
	public InheritanceResult result() {
		return new InheritanceResult(objResult);
	}

	@Override
	public void visitClass(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IClassInfo classInfo) {
		for(InheritanceDescription description : descriptors) {
			String parentClassName = description.getParentClassName();
			if(classInfo.inherits(scanResult, parentClassName)) {
				if(classInfo.access().isAbstract() && !description.allowsAbstractClasses()) {
					continue;
				}

				if(classInfo.access().isInterface() && !description.allowsInterfaces()) {
					continue;
				}

				if(!jsonResult.has(parentClassName)) {
					jsonResult.add(parentClassName, new JsonArray());
				}
				jsonResult.getAsJsonArray(parentClassName).add(classInfo.type().getClassName());
				objResult.computeIfAbsent(Type.getObjectType(parentClassName), k -> new ArrayList<>()).add(classInfo);

				jarInfo.addSummary(description, classInfo);
			}
		}

	}

	@Override
	public String getKey() {
		return InheritanceDescription.ID;
	}
}
