package org.uzene.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Reads meta information about the code using various methods. The final result is a Map<String,String> with all the
 * info it can gather.
 * 
 * 
 * See the following links to get some feeling of various methods to get this info.
 * 
 * https://github.com/jcabi/jcabi-manifests
 * 
 * http://www.yegor256.com/2014/07/03/how-to-read-manifest-mf.html
 * 
 * https://blog.oio.de/2011/12/09/accessing-maven-properties-from-your-code/
 * 
 * http://www.mojohaus.org/properties-maven-plugin/usage.html
 * 
 * @author raiser
 *
 */
public class AppInfo {
	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AppInfo.class);

	public static Map<String, String> readMetaInfo(Object object) {
		return readMetaInfo(object.getClass());
	}
	public static Map<String, String> readMetaInfo(Class<?> clazz) {
		return readMetaInfo(clazz, "", "");
	}
	public static Map<String, String> readMetaInfo(Class<?> clazz, String groupId, String artifactId) {
		logger.info("searching information for {} defined in groupId=[{}] artifactId=[{}]", clazz, groupId, artifactId);
		List<URL> urls = metaInfoLocations(clazz, groupId, artifactId);
		Map<String, Throwable> errors = new TreeMap<>();
		for (URL url : urls) {
			logger.info("analysing " + url + " ...");
			try (InputStream stream = url.openStream()) {
				logger.info("analysing " + url + " ... found stream " + stream.getClass());
				TreeMap<String, String> res = new TreeMap<>();
				if (url.getPath().endsWith("MANIFEST.MF")) {
					Manifest manifest = new Manifest(stream);
					// Attributes attr = manifest.getEntries().entrySet().stream().flatMap(x->x.getValue().);
					// TODO read all attributes
					Attributes attr = manifest.getMainAttributes();
					addEntries(res, attr.entrySet());
				} else if (url.getPath().endsWith(".properties")) {
					Properties p = new Properties();
					p.load(stream);
					addEntries(res, p.entrySet());
				} else if (url.getPath().endsWith("pom.xml")) {
					logger.debug("In the future pom.xml properties could be read. "
							+ "For now is better to use the org.codehaus.mojo:properties-maven-plugin:1.0.0"
							+ " and org.codehaus.mojo:buildnumber-maven-plugin:1.4");
				}
				return res;
			} catch (Throwable e) {
				errors.put(url.toExternalForm(), e);
			}
		}
		throw new RuntimeException("Couldn't find metaInfo. All errors are " + errors);
	}
	private static void addEntries(TreeMap<String, String> res, Set<Entry<Object, Object>> entrySet) {
		for (Entry<Object, Object> entry : entrySet) {
			res.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}
	private static List<URL> metaInfoLocations(Class<?> clazz, String groupId, String artifactId) {
		try {
			return Arrays.asList(new URL[] {
					new URL(javaResource(clazz,
							"META-INF/maven/" + groupId + "/" + artifactId + "/pom-build.properties")),
					new URL(javaResource(clazz, "META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties")),
					new URL(javaResource(clazz, "META-INF/maven/" + groupId + "/" + artifactId + "/pom.xml")),
					new URL(javaResource(clazz, "META-INF/MANIFEST.MF")),
					new URL(javaResource(clazz, "WEB-INF/MANIFEST.MF")), new URL(javaResource(clazz, "pom.xml")),
					new URL(javaResource(clazz, "../pom.xml")), new URL(javaResource(clazz, "../../pom.xml")) });
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	private static String javaResource(Class<?> clazz, String name) {
		return findParent(clazz) + name;
	}
	private static String findParent(Class<?> clazz) {
		String resName = "/" + clazz.getName().replace(".", "/") + ".class";
		String resLocation = clazz.getResource(resName).toExternalForm();
		// System.out.println(resLocation);
		String parent = resLocation.substring(0, resLocation.length() - resName.length() + 1);
		return parent;
	}
}
