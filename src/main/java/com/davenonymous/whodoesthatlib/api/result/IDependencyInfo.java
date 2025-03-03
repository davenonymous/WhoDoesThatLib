package com.davenonymous.whodoesthatlib.api.result;

import java.util.Optional;

public interface IDependencyInfo {
	String modId();

	boolean mandatory();

	Optional<String> versionRange();
}
