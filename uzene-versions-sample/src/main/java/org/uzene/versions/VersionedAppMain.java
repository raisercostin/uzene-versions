package org.uzene.versions;

import java.util.Map;
import java.util.Map.Entry;
import org.uzene.util.AppInfo;

public class VersionedAppMain {
	public static void main(String[] args) {
		Map<String, String> info = AppInfo.readMetaInfo(VersionedAppMain.class, "org.raisercostin",
				"uzene-versions-sample");
		System.out.println("VersionedAppMain info:");
		for (Entry<String, String> entry : info.entrySet()) {
			System.out.println("   "+entry);
		}
		Map<String, String> appInfoInfo = AppInfo.readMetaInfo(AppInfo.class, "org.raisercostin",
				"uzene-versions");
		System.out.println("AppInfo info:");
		for (Entry<String, String> entry : appInfoInfo.entrySet()) {
			System.out.println("   "+entry);
		}
	}
}
