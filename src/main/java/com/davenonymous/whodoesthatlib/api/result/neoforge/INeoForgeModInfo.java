package com.davenonymous.whodoesthatlib.api.result.neoforge;

import com.davenonymous.whodoesthatlib.api.result.IModInfo;

import java.util.Map;
import java.util.Optional;

public interface INeoForgeModInfo extends IModInfo {

	Optional<String> credits();

	Optional<Boolean> logoBlur();

	Optional<String> updateJSONURL();

	Optional<Map<String, Object>> customProperties();

}
