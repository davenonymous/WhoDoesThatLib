package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.MixinDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.MixinResult;
import com.google.gson.JsonArray;
import org.objectweb.asm.Type;

import java.util.ArrayList;

public class MixinClassAnalyzer extends ASMAnalyzer<MixinResult, Type, IClassInfo, MixinDescription> implements IModClassAnalyzer<MixinResult> {

	@Override
	public MixinResult result() {
		return new MixinResult(objResult);
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
			objResult.computeIfAbsent(targetClassType, k -> new ArrayList<>()).add(classInfo);

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
