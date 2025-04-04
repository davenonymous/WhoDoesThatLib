package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.descriptors.AnnotationDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IMethodInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.MethodAnnotationResult;
import com.davenonymous.whodoesthatlib.impl.serialize.StringyElementType;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MethodAnnotationAnalyzer extends AnnotationAnalyzer<MethodAnnotationResult, IMethodInfo> {
	@Override
	public StringyElementType elementType() {
		return StringyElementType.METHOD;
	}

	@Override
	public void visit(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IMethodInfo info) {
		for(AnnotationDescription annotation : filteredDescriptors) {
			Optional<IAnnotationInfo> matchingAnnotation = info.isAnnotatedWith(annotation.getAnnotationClassName());
			if(matchingAnnotation.isPresent()) {
				var annotationInfo = matchingAnnotation.get();
				var annotationClassName = annotation.getAnnotationClassName();
				if(!jsonResult.has(annotationClassName)) {
					jsonResult.add(annotationClassName, new JsonArray());
				}

				objResult.computeIfAbsent(annotationInfo.type(), k -> new ArrayList<>()).add(info);
				jsonResult.getAsJsonArray(annotationClassName).add(info.owner().type().getClassName());

				jarInfo.addSummary(annotation, info);
			}
		}
	}

	@Override
	public MethodAnnotationResult result() {
		return new MethodAnnotationResult(objResult);
	}
}
