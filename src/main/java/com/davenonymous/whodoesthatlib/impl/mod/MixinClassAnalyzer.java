package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.MixinDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.MixinResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MixinClassAnalyzer implements IModClassAnalyzer<MixinResult> {
	private JsonObject jsonResult;
	private Map<Type, List<IClassInfo>> result;
	private List<MixinDescription> descriptors;

	@Override
	public void onInit(IScanResult scanResult) {
		descriptors = scanResult.getSummaryDescriptions(MixinDescription.ID);
	}

	@Override
	public void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
		jsonResult = new JsonObject();
		result = new HashMap<>();
	}

	@Override
	public JsonElement encodedResult() {
		if(jsonResult.isEmpty()) {
			return JsonNull.INSTANCE;
		}
		return jsonResult;
	}

	@Override
	public MixinResult result() {
		return new MixinResult(result);
	}

	@Override
	public void visitClass(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IClassInfo classInfo) {
		var mixinAnnotation = classInfo.isAnnotatedWith("org.spongepowered.asm.mixin.Mixin");
		if(mixinAnnotation.isEmpty()) {
			return;
		}

		var annotationParams = mixinAnnotation.get().params();
		Object targetClass = annotationParams.get("value");
		if(targetClass instanceof Type targetClassType) {
			String targetClassName = targetClassType.getClassName();
			if(!jsonResult.has(targetClassName)) {
				jsonResult.add(targetClassName, new JsonArray());
			}
			jsonResult.getAsJsonArray(targetClassName).add(classInfo.type().getClassName());
			result.computeIfAbsent(targetClassType, k -> new ArrayList<>()).add(classInfo);

			for(MixinDescription description : descriptors) {
				if(description.getTargetClassName().equals(targetClassName)) {
					jarInfo.addSummary(description, classInfo);
				}
			}
		}
	}

	@Override
	public String getKey() {
		return MixinDescription.ID;
	}
}
