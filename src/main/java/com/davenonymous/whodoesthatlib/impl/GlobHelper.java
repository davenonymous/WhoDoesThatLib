package com.davenonymous.whodoesthatlib.impl;

import java.util.regex.PatternSyntaxException;

public class GlobHelper {
	private static final String regexMetaChars = ".^$+{[]|()";
	private static final String globMetaChars = "\\*?[{";
	private static char EOL = 0;

	private GlobHelper() {
	}

	private static boolean isRegexMeta(char c) {
		return ".^$+{[]|()".indexOf(c) != -1;
	}

	private static boolean isGlobMeta(char c) {
		return "\\*?[{".indexOf(c) != -1;
	}

	private static char next(String glob, int i) {
		return i < glob.length() ? glob.charAt(i) : EOL;
	}

	private static String toRegexPattern(String globPattern, boolean isDos) {
		boolean inGroup = false;
		StringBuilder regex = new StringBuilder("^");
		int i = 0;

		while(i < globPattern.length()) {
			char c = globPattern.charAt(i++);
			switch(c) {
				case '*':
					if(next(globPattern, i) == '*') {
						regex.append(".*");
						++i;
					} else if(isDos) {
						regex.append("[^\\\\]*");
					} else {
						regex.append("[^/]*");
					}
					break;
				case ',':
					if(inGroup) {
						regex.append(")|(?:");
					} else {
						regex.append(',');
					}
					break;
				case '/':
					if(isDos) {
						regex.append("\\\\");
					} else {
						regex.append(c);
					}
					break;
				case '?':
					if(isDos) {
						regex.append("[^\\\\]");
					} else {
						regex.append("[^/]");
					}
					break;
				case '[':
					if(isDos) {
						regex.append("[[^\\\\]&&[");
					} else {
						regex.append("[[^/]&&[");
					}

					if(next(globPattern, i) == '^') {
						regex.append("\\^");
						++i;
					} else {
						if(next(globPattern, i) == '!') {
							regex.append('^');
							++i;
						}

						if(next(globPattern, i) == '-') {
							regex.append('-');
							++i;
						}
					}

					boolean hasRangeStart = false;
					char last = 0;

					while(i < globPattern.length()) {
						c = globPattern.charAt(i++);
						if(c == ']') {
							break;
						}

						if(c == '/' || isDos && c == '\\') {
							throw new PatternSyntaxException("Explicit 'name separator' in class", globPattern, i - 1);
						}

						if(c == '\\' || c == '[' || c == '&' && next(globPattern, i) == '&') {
							regex.append('\\');
						}

						regex.append(c);
						if(c == '-') {
							if(!hasRangeStart) {
								throw new PatternSyntaxException("Invalid range", globPattern, i - 1);
							}

							if((c = next(globPattern, i++)) == EOL || c == ']') {
								break;
							}

							if(c < last) {
								throw new PatternSyntaxException("Invalid range", globPattern, i - 3);
							}

							regex.append(c);
							hasRangeStart = false;
						} else {
							hasRangeStart = true;
							last = c;
						}
					}

					if(c != ']') {
						throw new PatternSyntaxException("Missing ']", globPattern, i - 1);
					}

					regex.append("]]");
					break;
				case '\\':
					if(i == globPattern.length()) {
						throw new PatternSyntaxException("No character to escape", globPattern, i - 1);
					}

					char next = globPattern.charAt(i++);
					if(isGlobMeta(next) || isRegexMeta(next)) {
						regex.append('\\');
					}

					regex.append(next);
					break;
				case '{':
					if(inGroup) {
						throw new PatternSyntaxException("Cannot nest groups", globPattern, i - 1);
					}

					regex.append("(?:(?:");
					inGroup = true;
					break;
				case '}':
					if(inGroup) {
						regex.append("))");
						inGroup = false;
					} else {
						regex.append('}');
					}
					break;
				default:
					if(isRegexMeta(c)) {
						regex.append('\\');
					}

					regex.append(c);
			}
		}

		if(inGroup) {
			throw new PatternSyntaxException("Missing '}", globPattern, i - 1);
		} else {
			return regex.append('$').toString();
		}
	}

	public static String toUnixRegexPattern(String globPattern) {
		return toRegexPattern(globPattern, false);
	}

	public static String toWindowsRegexPattern(String globPattern) {
		return toRegexPattern(globPattern, true);
	}
}
