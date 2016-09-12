# uzene-versions

[![Download](https://api.bintray.com/packages/raisercostin/maven/uzene-versions/images/download.svg)](https://bintray.com/raisercostin/maven/uzene-versions/_latestVersion)
<!--
[![Build Status](https://travis-ci.org/raisercostin/uzene-versions.svg?branch=master)](https://travis-ci.org/raisercostin/uzene-versions)
[![Codacy Badge](https://www.codacy.com/project/badge/fe1bb28a7735433d89a238ce6f6305c1)](https://www.codacy.com/app/raisercostin/uzene-versions)
-->

## Desciption
A small class to get information about the jar/artifact containing a java class.

## Features
Reads meta information about the code using various methods. The final result is a Map<String,String> with all the info it can gather.

## What you get
You can get the info via ```AppInfo.readMetaInfo(aClass, optionalGroupId, optionalArtifactId)```
The output from sample app:
```
VersionedAppMain info:
   build-all=groupId=org.raisercostin, artifactId=uzene-versions-sample, version=0.1-SNAPSHOT, timestamp=2016-09-12T21-59-07-866-Z+0000, buildNumber=no-scm-configured
   build-id=org.raisercostin:uzene-versions-sample
   build-info=uzene-versions-sample-0.1-SNAPSHOT at 2016-09-12T21-59-07-866-Z+0000 Build#no-scm-configured
   build-timestamp=2016-09-12T21-59-07-866-Z+0000
   build-version=0.1-SNAPSHOT-2016-09-12T21-59-07-866-Z+0000-#no-scm-configured
   buildNumber=no-scm-configured
   maven.build.timestamp.format=yyyy-MM-dd'T'HH-mm-ss-SSS-'Z'Z
   scmBranch=UNKNOWN_BRANCH
   timestamp=1473717549223

AppInfo info:
   Archiver-Version=Plexus Archiver
   Build-Jdk=1.8.0_60
   Built-By=raiser
   Created-By=Apache Maven 3.3.9
   Implementation-Title=uzene-versions
   Implementation-URL=https://github.com/raisercostin/uzene-versions
   Implementation-Vendor-Id=org.uzene
   Implementation-Version=1.0
   Manifest-Version=1.0
   Specification-Title=uzene-versions
   Specification-Version=1.0
   build-all=groupId=org.uzene, artifactId=uzene-versions, version=1.0, timestamp=2016-09-12T21-54-32-436-Z+0000, buildNumber=f881d954ec33e13033acd12a4e0442a2abaaf2d9
   build-info=1.0-2016-09-12T21-54-32-436-Z+0000-#f881d954ec33e13033acd12a4e0442a2abaaf2d9
   build-timestamp=2016-09-12T21-54-32-436-Z+0000
   build-version=1.0-2016-09-12T21-54-32-436-Z+0000-#f881d954ec33e13033acd12a4e0442a2abaaf2d9
```
Notice the Implementation-URL and buildNumber=[f881d954ec33e13033acd12a4e0442a2abaaf2d9](../../commit/f881d954ec33e13033acd12a4e0442a2abaaf2d9).

## How to use it
See the [uzene-versions-sample](uzene-versions-sample) to see how you can configure this class in your project.

Add dependency on AppInfo class. The lib is hosted in JCenter

	<dependency>
		<groupId>org.uzene</groupId>
		<artifactId>uzene-versions</artifactId>
		<version>1.0</version>
	</dependency>

Configure in your pom.xml
- ```properties-maven-plugin``` and \<properties> section to add to jar the following properties file ```${project.build.outputDirectory}/META-INF/maven/${groupId}/${artifactId}/pom-build.properties```.
- ```buildnumber-maven-plugin``` to set the ${buildNumber} from scm(git,svn) revision. You need to have the <scm> section configured for this plugin.
- ```maven-jar-plugin``` to add properties to MANIFEST.MF .

## How it works
Information about a class is searched in various locations relative to that class. For now only the information from first existing resource is used.
The locations used are relative to the \<classContainerOrFolder>
- /META-INF/maven/\<groupId>/\<artifactId>/pom-build.properties		- uzene-versions convention
- /META-INF/maven/\<groupId>/\<artifactId>/pom.properties   - maven convention
- /META-INF/maven/\<groupId>/\<artifactId>/pom.xml   - maven convention, for now cannot extract info from here
- /META-INF/MANIFEST.MF   - sun convention used in jars, wars and other packages
- /WEB-INF/MANIFEST.MF   - servlet convention used for wars
- /pom.xml   - useful in development mode when the compiled classes are not bundled yet in a jar
- /../pom.xml   - useful in development mode when the compiled classes are not bundled yet in a jar
- /../../pom.xml   - useful in development mode when the compiled classes are not bundled yet in a jar

Full output clarifies also the sources of information.
```
23:59:24.068 [main] INFO  org.uzene.util.AppInfo - searching information for class org.uzene.versions.VersionedAppMain defined in groupId=[org.raisercostin] artifactId=[uzene-versions-sample]
23:59:24.189 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/META-INF/maven/org.raisercostin/uzene-versions-sample/pom-build.properties ...
23:59:24.189 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/META-INF/maven/org.raisercostin/uzene-versions-sample/pom-build.properties ... found stream class java.io.BufferedInputStream
23:59:24.192 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/META-INF/maven/org.raisercostin/uzene-versions-sample/pom.properties ...
23:59:24.192 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/META-INF/maven/org.raisercostin/uzene-versions-sample/pom.xml ...
23:59:24.192 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/META-INF/MANIFEST.MF ...
23:59:24.193 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/WEB-INF/MANIFEST.MF ...
23:59:24.193 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/pom.xml ...
23:59:24.193 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/../pom.xml ...
23:59:24.193 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/../../pom.xml ...
23:59:24.193 [main] INFO  org.uzene.util.AppInfo - analysing file:/Users/raiser/work/uzene-versions/uzene-versions-sample/target/classes/../../pom.xml ... found stream class java.io.BufferedInputStream
23:59:24.193 [main] DEBUG org.uzene.util.AppInfo - In the future pom.xml properties could be read. For now is better to use the org.codehaus.mojo:properties-maven-plugin:1.0.0 and org.codehaus.mojo:buildnumber-maven-plugin:1.4
VersionedAppMain info:
   build-all=groupId=org.raisercostin, artifactId=uzene-versions-sample, version=0.1-SNAPSHOT, timestamp=2016-09-12T21-59-07-866-Z+0000, buildNumber=no-scm-configured
   build-id=org.raisercostin:uzene-versions-sample
   build-info=uzene-versions-sample-0.1-SNAPSHOT at 2016-09-12T21-59-07-866-Z+0000 Build#no-scm-configured
   build-timestamp=2016-09-12T21-59-07-866-Z+0000
   build-version=0.1-SNAPSHOT-2016-09-12T21-59-07-866-Z+0000-#no-scm-configured
   buildNumber=no-scm-configured
   maven.build.timestamp.format=yyyy-MM-dd'T'HH-mm-ss-SSS-'Z'Z
   scmBranch=UNKNOWN_BRANCH
   timestamp=1473717549223
23:59:24.234 [main] INFO  org.uzene.util.AppInfo - searching information for class org.uzene.util.AppInfo defined in groupId=[org.raisercostin] artifactId=[uzene-versions]
23:59:24.236 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/META-INF/maven/org.raisercostin/uzene-versions/pom-build.properties ...
23:59:24.238 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/META-INF/maven/org.raisercostin/uzene-versions/pom.properties ...
23:59:24.238 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/META-INF/maven/org.raisercostin/uzene-versions/pom.xml ...
23:59:24.239 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/META-INF/MANIFEST.MF ...
23:59:24.239 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/META-INF/MANIFEST.MF ... found stream class sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream
23:59:24.241 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/WEB-INF/MANIFEST.MF ...
23:59:24.241 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/pom.xml ...
23:59:24.241 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/../pom.xml ...
23:59:24.241 [main] INFO  org.uzene.util.AppInfo - analysing jar:file:/Users/raiser/.m2/repository/org/uzene/uzene-versions/1.0/uzene-versions-1.0.jar!/../../pom.xml ...
AppInfo info:
   Archiver-Version=Plexus Archiver
   Build-Jdk=1.8.0_60
   Built-By=raiser
   Created-By=Apache Maven 3.3.9
   Implementation-Title=uzene-versions
   Implementation-URL=https://github.com/raisercostin/uzene-versions
   Implementation-Vendor-Id=org.uzene
   Implementation-Version=1.0
   Manifest-Version=1.0
   Specification-Title=uzene-versions
   Specification-Version=1.0
   build-all=groupId=org.uzene, artifactId=uzene-versions, version=1.0, timestamp=2016-09-12T21-54-32-436-Z+0000, buildNumber=f881d954ec33e13033acd12a4e0442a2abaaf2d9
   build-info=1.0-2016-09-12T21-54-32-436-Z+0000-#f881d954ec33e13033acd12a4e0442a2abaaf2d9
   build-timestamp=2016-09-12T21-54-32-436-Z+0000
   build-version=1.0-2016-09-12T21-54-32-436-Z+0000-#f881d954ec33e13033acd12a4e0442a2abaaf2d9
```

## Other solutions
See the following links to get some feeling of various methods to get this info.
- https://github.com/jcabi/jcabi-manifests
- http://www.yegor256.com/2014/07/03/how-to-read-manifest-mf.html
- https://blog.oio.de/2011/12/09/accessing-maven-properties-from-your-code/
- http://www.mojohaus.org/properties-maven-plugin/usage.html

## Development
### How to build and release

	mvn release:prepare release:perform -DskipTests=true -Prelease -Darguments="-DskipTests=true -Prelease"

