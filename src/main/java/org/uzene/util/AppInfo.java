package org.uzene.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.text.StrSubstitutor;
import com.jasongoodwin.monads.Try;

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
		logger.info("searching information for {} defined in groupId=[{}] artifactId=[{}]",clazz,groupId,artifactId);
		Stream<URL> urls = metaInfoLocations(clazz, groupId, artifactId);
		Stream<Try<Map<String, String>>> all2 = urls.map(url -> Try.ofFailable(() -> {
			logger.info("analysing " + url + " ...");
			try (InputStream stream = url.openStream()) {
				logger.info("analysing " + url + " ... found stream " + stream.getClass());
				TreeMap<String, String> res = new TreeMap<>();
				if (url.getPath().endsWith("MANIFEST.MF")) {
					Manifest manifest = new Manifest(stream);
					// Attributes attr = manifest.getEntries().entrySet().stream().flatMap(x->x.getValue().);
					// TODO read all attributes
					Attributes attr = manifest.getMainAttributes();
					attr.forEach((x, y) -> res.put(x.toString(), y.toString()));
				} else if (url.getPath().endsWith(".properties")) {
					Properties p = new Properties();
					p.load(stream);
					p.forEach((x, y) -> res.put(x.toString(), y.toString()));
				} else if (url.getPath().endsWith("pom.xml")) {
					logger.debug("In the future pom.xml properties could be read. "
							+ "For now is better to use the org.codehaus.mojo:properties-maven-plugin:1.0.0"
							+ " and org.codehaus.mojo:buildnumber-maven-plugin:1.4");
				}
				// System.out.println("properties="
				// + res.entrySet().stream().map(x -> x.getKey() + "=" +
				// x.getValue()).collect(Collectors.joining("\n")));
				return res;
			}
		}));
		List<Try<Map<String, String>>> all = all2.collect(Collectors.toList());
		Map<String, String> result = all.stream().filter(x -> x.isSuccess()).map(x -> x.getUnchecked()).findFirst()// TODO
																													// should
																													// collect
																													// all
				.orElseThrow(() -> new RuntimeException("Couldn't find metaInfo. All errors are "
						+ all.stream().map(x -> toString(getException(x))).collect(Collectors.joining("\n"))));
		return resolveAllVariables(result);
	}
	private static Map<String, String> resolveAllVariables(Map<String, String> map) {
		StrSubstitutor resolver = new StrSubstitutor(map);
		Map<String, String> result = new TreeMap<>();
		map.entrySet().stream().forEach(x -> {
			if (x.getValue().contains("$"))
				result.put(x.getKey(), resolver.replace(x.getValue()));
			else
				result.put(x.getKey(), x.getValue());
		});
		return result;
	}
	private static String toString(RuntimeException exception) {
		StringWriter s = new StringWriter();
		PrintWriter writer = new PrintWriter(s);
		exception.printStackTrace(writer);
		return s.getBuffer().toString();
	}
	private static RuntimeException getException(Try<?> x) {
		RuntimeException exception = null;
		try {
			x.getUnchecked();
		} catch (RuntimeException e) {
			exception = e;
		}
		if (exception == null)
			throw new Error("The try should contain an exception");
		return exception;
	}
	private static Stream<URL> metaInfoLocations(Class<?> clazz, String groupId, String artifactId) {
		try {
			return Stream.of(
					new URL(javaResource(clazz,
							"META-INF/maven/" + groupId + "/" + artifactId + "/pom-build.properties")),
					new URL(javaResource(clazz, "META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties")),
					new URL(javaResource(clazz, "META-INF/maven/" + groupId + "/" + artifactId + "/pom.xml")),
					new URL(javaResource(clazz, "META-INF/MANIFEST.MF")),
					new URL(javaResource(clazz, "WEB-INF/MANIFEST.MF")), new URL(javaResource(clazz, "pom.xml")),
					new URL(javaResource(clazz, "../pom.xml")), new URL(javaResource(clazz, "../../pom.xml")));
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
		//System.out.println(resLocation);
		String parent = resLocation.substring(0, resLocation.length() - resName.length() + 1);
		return parent;
	}
}
