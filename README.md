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

## How to use it
See the [uzene-versions-sample](uzene-versions-sample) to see how you can configure this class in your project.

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

## Other solutions
See the following links to get some feeling of various methods to get this info.
- https://github.com/jcabi/jcabi-manifests
- http://www.yegor256.com/2014/07/03/how-to-read-manifest-mf.html
- https://blog.oio.de/2011/12/09/accessing-maven-properties-from-your-code/
- http://www.mojohaus.org/properties-maven-plugin/usage.html

## Development
### How to build and release

	mvn release:prepare release:perform -DskipTests=true -Prelease -Darguments="-DskipTests=true -Prelease"

