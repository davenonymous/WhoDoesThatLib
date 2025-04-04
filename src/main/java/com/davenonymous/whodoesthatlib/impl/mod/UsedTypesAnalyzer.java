package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModClassAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.UsedTypeDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.UsedTypeResult;
import com.google.gson.JsonArray;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Set;

public class UsedTypesAnalyzer extends ASMAnalyzer<UsedTypeResult, Type, IClassInfo, UsedTypeDescription> implements IModClassAnalyzer<UsedTypeResult> {
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
