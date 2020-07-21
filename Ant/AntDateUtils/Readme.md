============================
1. CD to project root
$ ant compile
$ ant dist
$ ant clean
$ ant ----Will execute the default target

Test:
$ java -cp dist/DateUtils-20141030.jar com.mkyong.core.utils.DateUtils
$ java -jar dist/DateUtils-20141030.jar

=============================Install Ivy and Resolve the dependencies first
1. Create logback.xml and ivy.xml file in project root
$ ant ivy --------download the ivy module from Maven center repository to local ${user.home}/.ant/lib/ivy.jar.
$ ant resolve ----The declared libraries will be downloaded to the project lib folder.

Review the updated build.xml script. Main points :

Manage the project external libraries with Apache Ivy, review the ivy namespace on top, and task “resolve”.
To compile the source code, you need to declares the classpath. Review task “compile”, and “classpathref” attribute.
In “jar” task, constructs the entire list of the external libraries and put it into the manifest.mf file.
In “jar” task, the project jar will be packaged to folder “dist” and the entire external libraries will be copied to from “lib” to “dist/lib”.

Test:
5.1 Jar it.
$ ant
5.2 Inspects the generated jar file.
$ jar -tf dist/DateUtils.jar 
5.3 Run the Jar file.
$ java -jar dist/DateUtils.jar 
5.4 Run the Jar file again, with logback.xml.
$ java -jar -Dlogback.configurationFile=src/logback.xml dist/DateUtils.jar

=============================
we will show you how to use Ant build script to create a big far / uber Jar file, which mean include the entire project external dependencies into a single jar file.
$ ant
$ java -jar dist/DateUtils.jar 
$ ant print-classpath


=============================Ant Contrib JAR
1. Put (latest) ant-contrib-1.0b3.jar in ${user.home}/.ant and ANT_HOME/lib to load <taskdef resource="net/sf/antcontrib/antlib.xml"/>
2. Older version does not have antlib.xml, rather they have antcontrib.properties.

