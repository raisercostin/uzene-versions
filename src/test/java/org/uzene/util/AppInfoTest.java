package org.uzene.util;

import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Test;

public class AppInfoTest {

	@Test
	public void testLoggerVersion() {
		Map<String, String> info = AppInfo.readMetaInfo(org.slf4j.LoggerFactory.class);
		Assert.assertEquals("1.7.21", info.get("Bundle-Version"));
		System.out.println("properties=" + toString(info));
	}
	@Test
	public void testJunitVersion() {
		Map<String, String> info = AppInfo.readMetaInfo(org.junit.Test.class);
		System.out.println("properties=" + toString(info));
		Assert.assertEquals("4.12", info.get("Implementation-Version"));
	}
	@Test
	public void testOwnVersionBuiltAsInSample() {
		Map<String, String> info = AppInfo.readMetaInfo(AppInfo.class, "org.uzene", "uzene-versions");
		System.out.println("properties=" + toString(info));
		Assert.assertEquals("org.uzene:uzene-versions", info.get("build-id"));
	}
	private String toString(Map<String, String> info) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : info.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
		}
		return sb.toString();
	}
}
