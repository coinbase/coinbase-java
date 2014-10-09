#!/bin/bash

VERSION=$1

mvn clean
mvn compile
mvn package
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=pom.xml -Dfile=target/coinbase-java-$VERSION.jar
mvn javadoc:jar
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=pom.xml -Dfile=target/coinbase-java-$VERSION-javadoc.jar -Dclassifier=javadoc
mvn source:jar
mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=pom.xml -Dfile=target/coinbase-java-$VERSION-sources.jar -Dclassifier=sources

