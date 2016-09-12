package org.uzene.util;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class AppInfoTest {

	@Test
	public void testLoggerVersion() {
		Map<String, String> info = AppInfo.readMetaInfo(org.slf4j.LoggerFactory.class);
		Assert.assertEquals("1.7.21", info.get("Bundle-Version"));
		System.out.println("properties="
				+ info.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue()).collect(Collectors.joining("\n")));
	}
	@Test
	public void testJunitVersion() {
		Map<String, String> info = AppInfo.readMetaInfo(org.junit.Test.class);
		System.out.println("properties="
				+ info.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue()).collect(Collectors.joining("\n")));
		Assert.assertEquals("4.12", info.get("Implementation-Version"));
	}
	@Test
	public void testOwnVersionBuiltAsInSample() {
		Map<String, String> info = AppInfo.readMetaInfo(AppInfo.class,"org.uzene","uzene-versions");
		System.out.println("properties="
				+ info.entrySet().stream().map(x -> x.getKey() + "=" + x.getValue()).collect(Collectors.joining("\n")));
		Assert.assertEquals("org.uzene:uzene-versions", info.get("build-id"));
	}
}
