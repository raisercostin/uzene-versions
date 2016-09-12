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
	}
}
