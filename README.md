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
    - Do `SET JAVA_HOME=<where you install jdk11`
    - Do `SET PATH=%JAVA_HOME%\bin;%PATH%`
  * On linux:
    - Do `export JAVA_HOME=<where you install jdk11`
    - Do `export $PATH=$JAVA_HOME/bin:$PATH`
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

```
  <maven.compiler.plugin.version>3.8.0</maven.compiler.plugin.version>
  <maven.surfire.plugin.version>2.22.1</maven.surfire.plugin.version>
```

## Add Automatic-Module-Name to Manifest.MF
To prepare for modules, we start by reserving the module names. This is done by setting `Automatic-Module-Name` in the Manifest.mf file. This is most easily done by configuring hte maven jar plugin.

All java modules (api and service) need to configure maven jar plugin to add Automatic-Module-Name to the `META-INF/MANIFEST.MF` file in the generated jar files.
In the respective pom files, add the following plugin configuration:
```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <archive>
                        <manifestEntries>
                            <Automatic-Module-Name>Your module name</Automatic-Module-Name>
                        </manifestEntries>
                    </archive>
                </configuration>
            </plugin>
        </plugins>
    </build>

```      

Module names should be `com.jpmsworkshop.students.api` and `com.jpmsworkshop.students.service` for the two maven modules.

# Modularize the application
To modularize a maven module, we just add a module descriptor file `module-info.java` in the `src/main/java` folder for that module.
```
module com.jpmsworkshop.students.<modulename> {

}
```
Modern IDEs and the compiler will change behaviour when this file is found, and you will at once get lot of compiler errors like: 
```
Error:(3, 12) java: package java.sql is not visible
  (package java.sql is declared in module java.sql, but module com.jpmsworkshop.students.service does not read it)
```
In the module descriptor file for the students service module, we have to declare a dependency to the `java.sql` module. 
This dependency is in addition to the maven dependency in the `pom.xml` file. 

Go ahead and add dependencies to the module descriptors. Find the module name by:
1. Check the jar files `META-INF/MANIFEST.mf` til for an `Automatic-Module-Name` entry or
2. Add it by using the name of the jar file without version number.

Example: `requires spring.boot;`

## Fix compilation errors in this project
You will probably see a compilation error like:
```
 jpms-workshop/service/src/main/java/com/jpmsworkshop/students/SpringBootJpmsApplication.java:[1,1] 
 package exists in another module: com.jpmsworkshop.students.api
```

This is because two classes from different modules (Student and SpringBootJpmsApplication) both are located in the same package, com.jpmsworkshop.students.
This is not allowed. Package names must be unique for a module. We can fix this by moving the class `com.jpmsworkshop.students.Student` to a new package
`com.jpmsworkshop.students.api.Student`. You must probably also fix imports where this class is used and module descriptor.

## Problems with SpringFox
The same problem as with Student above will also be found with dependencies from SpringFox. As SpringFox is not our dependency and we do not have access to the source code, 
we have no other option than to remove this dependency and use something else or wait for the owners to fix their code in such way that it supports java modules.