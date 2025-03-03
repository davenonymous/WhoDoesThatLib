package com.davenonymous.whodoesthatlib.impl;

import java.util.*;

public class LicenseHelper {
	private static final Map<String, String> licenses = new HashMap<>();
	private static final List<String> licenseKeywords = new ArrayList<>();

	public static Optional<String> getLicenseFromString(String input) {
		if(input == null || input.isEmpty()) {
			return Optional.empty();
		}

		String lowerInput = input.toLowerCase(Locale.ROOT);
		Optional<String> matchingLicenseKey = licenseKeywords.stream().filter(lowerInput::contains).findFirst();

		return matchingLicenseKey.map(licenses::get);
	}

	static {
		licenses.put("AFL-3.0".toLowerCase(), "Academic Free License v3.0");
		licenses.put("Apache-2.0".toLowerCase(), "Apache license 2.0");
		licenses.put("Apache".toLowerCase(), "Apache license family");
		licenses.put("Artistic-2.0".toLowerCase(), "Artistic license 2.0");
		licenses.put("BSL-1.0".toLowerCase(), "Boost Software License 1.0");
		licenses.put("BSD-2-Clause".toLowerCase(), "BSD 2-clause \"Simplified\" license");
		licenses.put("BSD-3-Clause".toLowerCase(), "BSD 3-clause \"New\" or \"Revised\" license");
		licenses.put("BSD-3-Clause-Clear".toLowerCase(), "BSD 3-clause Clear license");
		licenses.put("BSD-4-Clause".toLowerCase(), "BSD 4-clause \"Original\" or \"Old\" license");
		licenses.put("0BSD".toLowerCase(), "BSD Zero-Clause license");
		licenses.put("BSD".toLowerCase(), "BSD license family");
		licenses.put("CC".toLowerCase(), "Creative Commons");
		licenses.put("CC0-1.0".toLowerCase(), "Creative Commons Zero v1.0 Universal");
		licenses.put("CC-BY-4.0".toLowerCase(), "Creative Commons Attribution 4.0");
		licenses.put("CC-BY-SA-4.0".toLowerCase(), "Creative Commons Attribution ShareAlike 4.0");
		licenses.put("WTFPL".toLowerCase(), "Do What The F*ck You Want To Public License");
		licenses.put("ECL-2.0".toLowerCase(), "Educational Community License v2.0");
		licenses.put("EPL-1.0".toLowerCase(), "Eclipse Public License 1.0");
		licenses.put("EPL-2.0".toLowerCase(), "Eclipse Public License 2.0");
		licenses.put("EUPL-1.1".toLowerCase(), "European Union Public License 1.1");
		licenses.put("AGPL-3.0".toLowerCase(), "GNU Affero General Public License v3.0");
		licenses.put("GNU".toLowerCase(), "GNU General Public License family");
		licenses.put("GPL".toLowerCase(), "GNU General Public License family");
		licenses.put("GPL-2.0".toLowerCase(), "GNU General Public License v2.0");
		licenses.put("GPL-3.0".toLowerCase(), "GNU General Public License v3.0");
		licenses.put("LGPL".toLowerCase(), "GNU Lesser General Public License family");
		licenses.put("LGPL2".toLowerCase(), "GNU Lesser General Public License v2");
		licenses.put("LGPL-2.1".toLowerCase(), "GNU Lesser General Public License v2.1");
		licenses.put("LGPL-3.0".toLowerCase(), "GNU Lesser General Public License v3.0");
		licenses.put("ISC".toLowerCase(), "ISC");
		licenses.put("LPPL-1.3c".toLowerCase(), "LaTeX Project Public License v1.3c");
		licenses.put("MS-PL".toLowerCase(), "Microsoft Public License");
		licenses.put("MIT".toLowerCase(), "MIT");
		licenses.put("MPL".toLowerCase(), "Mozilla Public License family");
		licenses.put("MPL-2.0".toLowerCase(), "Mozilla Public License 2.0");
		licenses.put("OSL-3.0".toLowerCase(), "Open Software License 3.0");
		licenses.put("PostgreSQL".toLowerCase(), "PostgreSQL License");
		licenses.put("OFL-1.1".toLowerCase(), "SIL Open Font License 1.1");
		licenses.put("NCSA".toLowerCase(), "University of Illinois/NCSA Open Source License");
		licenses.put("Unlicense".toLowerCase(), "The Unlicense");
		licenses.put("Zlib".toLowerCase(), "zLib License");

		licenses.put("github".toLowerCase(), "GitHub Repository");
		licenses.put("MCPL".toLowerCase(), "Minecraft Mod Public License");

		var clonedLicenses = licenses.values().stream().toList();
		clonedLicenses.forEach(s -> licenses.put(s.toLowerCase(), s));

		licenseKeywords.addAll(licenses.keySet().stream().sorted(Comparator.comparing(String::length, Comparator.reverseOrder())).toList());
	}
}
