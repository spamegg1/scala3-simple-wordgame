val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple-wordgame",
    version := "0.1.0",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.11" % "test",
      ("org.typelevel" %% "cats-core" % "2.3.0").cross(CrossVersion.for3Use2_13),
      ("org.typelevel" %% "cats-effect" % "3.1.1").cross(CrossVersion.for3Use2_13)
    )
  )
