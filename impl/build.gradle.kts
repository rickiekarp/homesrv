plugins {
  id("org.gradle.java.experimental-jigsaw") version "0.1.1"
}

//javaModule.name = "impl"

dependencies {
  compile(project(":api"))
}
