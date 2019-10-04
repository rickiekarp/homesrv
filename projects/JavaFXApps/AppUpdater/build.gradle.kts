import java.util.Date
import java.text.SimpleDateFormat

plugins {
    id("org.gradle.java.experimental-jigsaw") version "0.1.1"
    application
}

apply {
    plugin("java")
}

application {
    applicationName = "Updater"
    mainClassName="net.rickiekarp.appupdater.UpdateMain"
    group = "net.rickiekarp.appupdater"
    version = "1.0-SNAPSHOT"
}

//javaModule {
//    setName("updater")
//}

dependencies {
    compile(project(":core"))
}

tasks {
    withType<Jar> {
        println("Building " + project.name)

        val minorVersion = 0

        val publicVersion = if (minorVersion > 0) {
            SimpleDateFormat("yy.MM").format(Date()) + '.' + minorVersion
        } else {
            SimpleDateFormat("yy.MM").format(Date())
        }

        manifest {
            attributes["Main-Class"] = application.mainClassName
            attributes["Build-Time"] = SimpleDateFormat("yyMMddHHmm").format(Date())
            attributes["Version"] = publicVersion
        }

//        archiveName = "${javaModule.geName()}-$publicVersion.jar"
    }
}