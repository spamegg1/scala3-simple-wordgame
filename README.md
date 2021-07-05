## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

# Setup and Installation
These are for Windows; Linux and MacOS should be similar/easier.

1. Download and install `AdoptOpenJDK` Version 11 at https://adoptopenjdk.net/ Make sure to choose the options to add it to `PATH` and set `JAVA_HOME`.

  ![01](images/01.png)

2. Download and install `JetBrains IntelliJ IDEA` at https://www.jetbrains.com/idea/

  ![02](images/02.png)

3. Reboot.

  ![03](images/03.png) 

4. Download and install `sbt` version 1.5.4 or later (the version is important! It won't work otherwise) at https://www.scala-sbt.org/download.html (you need the `.msi` installer for Windows).

  ![04](images/04.png) 

5. Launch IntelliJ, to go `Plugins` and install the `Scala` plugin.

  ![05](images/05.png)  

6. Restart IntelliJ.

  ![06](images/06.png) 

7. Go to IntelliJ Settings, `Build, Execution and Deployment/Build Tools/sbt` and point it to your `AdoptOpenJDK` and `sbt` installations:

  ![07](images/07.png) 

8. Download the repository zip file:

  ![08](images/08.png) 

9. Extract it to `C:\scala3-simple-wordgame-master`.

  ![09](images/09.png) 

10. In IntelliJ, click "Open" and navigate to the `build.sbt` file:

  ![10](images/10.png) 

11. Choose "Open as Project":

  ![11](images/11.png) 

12. Choose to "Trust":

  ![12](images/12.png) 

13. Now wait for about 5 minutes.

  ![13](images/13.png) 

14. Eventually it will finish.

  ![14](images/14.png) 

15. Open "Terminal" and type `sbt`. Then type `compile`.

  ![15](images/15.png) 

16. Type `test` to run the tests.

  ![16](images/16.png) 

17. Type `run` to play the game!

  ![17](images/17.png) 