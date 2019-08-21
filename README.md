# Preparations:
* Clone this project to your computer
* Download and install JDK11 

* Verify that it's working fine with java 8:
  - run `mvn clean install` from command line
  - run `mvn spring-boot:run --projects=service`
  
* Open the swagger UI in the browser:
  - http://localhost:8080/swagger-ui.html
  
# Make Java 11 the active Java
  * On windows:
    - `SET JAVA_HOME=<where you install jdk11`
    - `SET PATH=%JAVA_HOME%\bin;%PATH%`
  * On linux:
    - `export JAVA_HOME=<where you install jdk11`
    - `export $PATH=$JAVA_HOME/bin:$PATH`

Run `java --version` and `mvn --version` to see that your are using the
right JDK version and mvn is also using JDK 11.

* Verify that it's working fine with java 11:
  - run `mvn clean install` from command line
  - run `mvn spring-boot:run --projects=service`

## Set java version to 11 in pom file
In the parent pom there is a property telling maven which Java version to use.
Note that since Java 9, the property has changed name to  `java.version`. So in the `properties` section, add
` <java.version>11</java.version>` or replace 11 with your version.

## Update maven plugins
When migration to later Java versions, make sure all your maven plugins are up-to-date. Java 9, 10 and 11 support are
typically supported only in later versions of the plugins.

Add the following propertis in the parent pom.
```
  <maven.compiler.plugin.version>3.8.1</maven.compiler.plugin.version>
  <maven.surfire.plugin.version>2.22.1</maven.surfire.plugin.version>
```

In the `pluginManagement` section make sure the correct version of the `compiler-plugin`
and `surefire-plugin` are used.

## Add Automatic-Module-Name to Manifest.MF
To prepare for modules, we start by reserving the module names, which is done by 
setting `Automatic-Module-Name` in the Manifest.mf file. This is most easily done by configuring the maven jar plugin.

In `pluginManagement` in parent pom, add `jar-plugin` with latest version. Feel free to make a property 
of the version number.
 
```
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>3.1.2</version>
    </plugin>
```

In the `plugins` section (not `pluginManagement`) in the parent `pom.xml` file,
jar plugin. 
```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <configuration>
                    <archive>
                        <manifestEntries>
                            <Automatic-Module-Name>${module.name}</Automatic-Module-Name>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>
```      
The `jar-plugin` will now run for each module, including the parent, so all three
`pom.xml` files needs to have it's own property named `module.name` to be used by the plugin.

Module names should be `com.jpmsworkshop.students.api` and 
`com.jpmsworkshop.students.service` for the two maven sub modules. and can be
`com.jpmsworkshop.students.parent` for the parent pom.

Run `mvn clean install`, extract the `META-INF/MANIFEST.MF` file from the resulting
jar files (in the target folder in the modules) and verify that the property
`Automatic-Module-Name` is set to your module name.
 
# Modularize the application
To modularize a maven module, we just add a module descriptor file `module-info.java` in the `src/main/java` folder for each module.
```
module com.jpmsworkshop.students.<modulename> {

}
```
Let the modulenames be `api` and `service`.

Modern IDEs and the compiler will change behaviour when this file is found, and you will at once get lots of compiler errors like: 
```
Error:(3, 12) java: package java.sql is not visible
  (package java.sql is declared in module java.sql, but module com.jpmsworkshop.students.service does not read it)
```

In the module descriptor file for the students `service` module, we have to declare the the `service` module requires 
the `java.sql` module. 
Note: This has to be done in addition to the maven dependency in the `pom.xml` file. 

Go ahead and add dependencies to the module descriptors. Find the module name by:
1. Check the jar files `META-INF/MANIFEST.mf` til for an `Automatic-Module-Name` entry or
2. Add it by using the name of the jar file without version number and extension (`json-path-2.4.0.jar` becomes `json.path`).

Example: `requires json.path;`

After writing a few requires statements, note that later versions of IntelliJ lets you
place the cursor on a "red" import statement and `alt-enter` to add requires to the module descriptor.

# Package exists in several modules
You will probably see compilation errors like:
```
Error:(1, 1) java: package exists in another module: com.jpmsworkshop.students.api
Error:java: module spring.boot.autoconfigure reads package springfox.documentation.schema from both springfox.schema and springfox.core
```
## Fixing conflicts in our own code
In our code, Student (api module) and SpringBootJpmsApplication (service module) are both located in the same package, 
`com.jpmsworkshop.students`, which is not allowed. In code we control,  we can fix this by moving the class 
`com.jpmsworkshop.students.Student` to a new package `com.jpmsworkshop.students.api.Student`. Adding `api` to the package
makes the package unique to that module. Remember to fix imports and module descriptor after refactoring 
if the IDE does not do it for you.


## Problems with 3rd party libraries (e.g. SpringFox)
For 3rd party libraries it's not easy to fix problems with same package in several modules. Let's look inside two
jar files from springfox:
```
springfox-core-2.9.2.jar:
    springfox.documentation.schema.Entry
    springfox.documentation.schema.Enums
    ...
springfox-schema-2.9.2.jar:
    springfox.documentation.schema.Annotations
    springfox.documentation.schema.Maps
    ...
```
Both `core` and `schema` jar files contain classes in the `springfox.documentation.schema` package. In these cases
our options are limited:

1. Upgrade to a newer version _may_ solve the problem
2. Fix the problem in the library (if it's open source)
3. Replace or remove the library if possible
4. Re-package the jar files to _one_ jar file and, potentially, add your own module descriptor
   * Maven shade or assembly plugin may help
5. Skip modularization (you can still compile and run with Java 11/12)   

For the purpose of this workshop, just remove everything springfox related 
(dependencies, imports and code).

# Running the application 
Run the application `mvn spring-boot:run --projects=service` or start it from the IDE.
It runs fine, but without swagger-ui, you have to use the brower, or other tools
like curl or Postman to test the API. Open a browser and type `http://localhost:8080/students`.
You should see the Json representing two student.

# Testing the application
When running tests, we encounter a different problem. `SpringBootTest` needs to do reflection on our 
classes in the service module, but this module do not open to reflection (runtime access). 
To resolve the problem, you have two options:

## Open the whole module to reflection
When migrating an application to the module system, it's easier to open
the whole package to reflection. Note that this will not make the packages
available compiletime. 
```$xslt
open module com.jpmsworkshop.students.service{
    ...
}
```

## Open specific packages to reflection
If you know which packages that spring needs access to, you can open only them.
In our small, simple application we can open the `com.jpmsworkshop.students` package. 

```$xslt
module com.jpmsworkshop.students.service{
    opens com.jpmsworkshop.students;
    ...
}
```

The test should now run green.