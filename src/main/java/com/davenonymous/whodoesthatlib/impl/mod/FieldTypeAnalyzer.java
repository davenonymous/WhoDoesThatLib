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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldTypeAnalyzer implements IModFieldAnalyzer<FieldResult> {
	private JsonObject jsonResult;
	private Map<Type, List<IFieldInfo>> fieldResultBuilder;
	private List<FieldTypeDescription> descriptors;

	@Override
	public void onInit(IScanResult scanResult) {
		descriptors = scanResult.getSummaryDescriptions(getKey());
	}

	@Override
	public void onJarStart(IScanResult scanResult, IJarInfo jarInfo) {
		jsonResult = new JsonObject();
		fieldResultBuilder = new HashMap<>();
	}

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
			fieldResultBuilder.computeIfAbsent(Type.getObjectType(description.getFieldClassName()), k -> new ArrayList<>()).add(fieldInfo);

			jarInfo.addSummary(description, fieldInfo);
		}
	}

	@Override
	public JsonElement encodedResult() {
		if(jsonResult.isEmpty()) {
			return JsonNull.INSTANCE;
		}
		return jsonResult;
	}

	@Override
	public FieldResult result() {
		return new FieldResult(fieldResultBuilder);
	}

	@Override
	public String getKey() {
		return FieldTypeDescription.ID;
	}
}
