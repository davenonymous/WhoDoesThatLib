package com.davenonymous.whodoesthatlib.api.result;

import java.util.List;
import java.util.Optional;

public interface IModInfo {
	String modId();

	String displayName();

	String description();

	String version();

	Optional<String> authors();

	Optional<String> logoFile();

	Optional<String> modUrl();

	List<IDependencyInfo> dependencies();
}
