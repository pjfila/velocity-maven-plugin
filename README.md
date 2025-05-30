# README

## Introduction

The plugin name is velocity-maven-plugin and there is a single goal: `velocity`.

Version pjfila-1.1.0
* New VelocityContext for each generated file
* Add extraProperties configuration
* Add removePrefix configuration
* Added unit tests + refactor parts

Version pjfila-1.0.0 (forked from [vdubus/velocity-maven-plugin](https://github.com/vdubus/velocity-maven))
* Upgrade Velocity to version 2.4.1

## Example Addition to POM

```xml
<plugin>
	<groupId>io.github.pjfila</groupId>
	<artifactId>velocity-maven-plugin</artifactId>
	<version>1.1.0</version>
	<executions>
		<execution>
			<id>Generate sources with Apache Velocity</id>
			<phase>generate-sources</phase>
			<goals>
				<goal>velocity</goal>
			</goals>
			<configuration>
				<removeExtension>.vm</removeExtension>
				<templateFiles>
					<directory>src/main/resources</directory>
					<includes>
						<include>**/*.vm</include>
					</includes>
				</templateFiles>
				<templateValues>
					<test>testValue</test>
				</templateValues>
			</configuration>
		</execution>
	</executions>
</plugin>
```

## Options

| Option Name     | Default                         | Notes                                                                                                             |
|-----------------|---------------------------------|-------------------------------------------------------------------------------------------------------------------|
| encoding        | ${project.build.sourceEncoding} | This option also has null check that sets the value to "UTF-8"                                                    |
| outputDirectory | ${project.build.directory}      |                                                                                                                   |
| removeExtension | no default                      | Remove an unwanted extension when saving result. For example foo.xml.vtl ==> foo.xml if removeExtension = '.vtl'. |
| removePrefix    | no default                      | Remove an unwanted prefix when saving result. For example foo.xml.vtl ==> xml.vtl if removePrefix = 'foo.'.       |
| templateFiles   | Required, no default.           |                                                                                                                   |
| templateValues  | Required, no default.           | This is the properties list you wish to have merged with your templates                                           |
| extraProperties | no default                      | Extra properties to set for the Velocity Engine                                                                   |
