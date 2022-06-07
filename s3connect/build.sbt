name := "s3connect"

version := "1.0"

scalaVersion := "2.13.8"

libraryDependencies ++= {

  lazy val minioVersion = "6.0.13"
  lazy val commonsioVersion = "2.5"
  lazy val jossVersion = "0.10.2"

  Seq(
    "io.minio"              % "minio"                   % minioVersion,
    "commons-io"            % "commons-io"              % commonsioVersion,
    "org.javaswift"         % "joss"                    % jossVersion,
    "com.amazonaws"         % "aws-java-sdk"            % "1.11.750",
    "org.slf4j"             % "slf4j-api"               % "1.7.5",
    "ch.qos.logback"        % "logback-classic"         % "1.0.9"
  )

}

/*resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)*/