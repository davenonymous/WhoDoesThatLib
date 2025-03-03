package com.davenonymous.whodoesthatlib.impl.mod;

import com.davenonymous.whodoesthatlib.api.descriptors.AnnotationDescription;
import com.davenonymous.whodoesthatlib.api.result.IJarInfo;
import com.davenonymous.whodoesthatlib.api.result.IModInfo;
import com.davenonymous.whodoesthatlib.api.result.IScanResult;
import com.davenonymous.whodoesthatlib.api.result.asm.IAnnotationInfo;
import com.davenonymous.whodoesthatlib.api.result.asm.IFieldInfo;
import com.davenonymous.whodoesthatlib.api.result.mod.FieldAnnotationResult;
import com.davenonymous.whodoesthatlib.impl.serialize.StringyElementType;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class FieldAnnotationAnalyzer extends AnnotationAnalyzer<FieldAnnotationResult, IFieldInfo> {

	@Override
	public void visit(IScanResult scanResult, IJarInfo jarInfo, IModInfo modInfo, IFieldInfo info) {
		List<AnnotationDescription> descriptors = scanResult.getSummaryDescriptions(getKey());
		List<AnnotationDescription> filteredDescriptors = descriptors.stream()
			.filter(descriptor -> descriptor.getElementType().equals(StringyElementType.FIELD))
			.sorted(Comparator.comparing(AnnotationDescription::getAnnotationClassName))
			.toList();

		for(AnnotationDescription annotation : filteredDescriptors) {
			Optional<IAnnotationInfo> matchingAnnotation = info.isAnnotatedWith(annotation.getAnnotationClassName());
			if(matchingAnnotation.isPresent()) {
				var annotationInfo = matchingAnnotation.get();
				var annotationClassName = annotation.getAnnotationClassName();
				if(!jsonResult.has(annotationClassName)) {
					jsonResult.add(annotationClassName, new JsonArray());
				}

				eventResultBuilder.computeIfAbsent(annotationInfo.type(), k -> new ArrayList<>()).add(info);
				jsonResult.getAsJsonArray(annotationClassName).add(info.owner().type().getClassName() + "#" + info.name());

				jarInfo.addSummary(annotation, info);
			}
		}
	}

	@Override
	public FieldAnnotationResult result() {
		return new FieldAnnotationResult(eventResultBuilder);
	}
}
