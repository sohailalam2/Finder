echo off
set Finder=.\finder.jar
set PORT="5566"
if exist "%Finder%" goto Finder
echo Could not locate the Finder JAR FILE. MAKE SURE YOU EXECUTE THIS SCRIPT FROM THE DIRECTORY OF THE JAR FILE
GOTO END

:Finder

set JAVA=%JAVA_HOME%\bin\java
set JAVAC_JAR=%JAVA_HOME%\lib\tools.jar

"%JAVA%" -server -version 2>&1 | findstr /I hotspot > nul
if not errorlevel == 1 (set JAVA_OPTS=%JAVA_OPTS% -server)
set JAVA_OPTS=%JAVA_OPTS% -server -Xms1G -Xmx1G -Xmn128M -Xss240K -XX:MaxDirectMemorySize=256M -XX:MaxNewSize=256M -XX:-UseParallelGC -XX:ParallelGCThreads=12 -XX:MaxPermSize=512M -XX:+UseNUMA -XX:+UseFastAccessorMethods -XX:+AggressiveOpts -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:-HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heapdump.hprof -Djava.net.preferIPv4Stack=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.port=%PORT% -Dio.netty.allocator.numDirectArenas=16
if not "%JAVAC_JAR%" == "" set IMSASJAR=%JAVAC_JAR%;%Finder%
set RUN_CLASSPATH=%Finder%
if "%RUN_CLASSPATH%" == "" set RUN_CLASSPATH=%Finder%

set AS_CLASSPATH=%RUN_CLASSPATH%

"%JAVA%" %JAVA_OPTS% ^
 -classpath "%AS_CLASSPATH%" ^
com.sohail.alam.finder.Finder%*

:END
