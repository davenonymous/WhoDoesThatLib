package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.analyzers.IModFieldAnalyzer;
import com.davenonymous.whodoesthatlib.api.descriptors.FieldTypeDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IClassInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.FieldResult;
import com.google.gson.JsonArray;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class FieldTypeAnalyzer extends ASMAnalyzer<FieldResult, Type, IFieldInfo, FieldTypeDescription> implements IModFieldAnalyzer<FieldResult> {

	@Override
	public void visitField(IScanResult scanResult, IJarInfo jarInfo, @Nullable IModInfo modInfo, IClassInfo classInfo, IFieldInfo fieldInfo) {
		String fieldTypeClassName = fieldInfo.type().getClassName();
		for(var description : descriptors) {
			if(!scanResult.doesInheritFrom(fieldTypeClassName, description.getFieldClassName())) {
				continue;
			}

			if(!jsonResult.has(description.getFieldClassName())) {
				jsonResult.add(description.getFieldClassName(), new JsonArray());
			}
			jsonResult.getAsJsonArray(description.getFieldClassName()).add(fieldInfo.locatableName());
			objResult.computeIfAbsent(Type.getObjectType(description.getFieldClassName()), k -> new ArrayList<>()).add(fieldInfo);

			jarInfo.addSummary(description, fieldInfo);
		}
	}

	@Override
	public FieldResult result() {
		return new FieldResult(objResult);
	}

	@Override
	public String getKey() {
		return FieldTypeDescription.ID;
	}
}
