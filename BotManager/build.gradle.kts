group 'net.rickiekarp.botmanager'

def minorVersion = 0

configurations {
    provided
    // Make compile extend from our provided configuration so that things added to bundled end up on the compile classpath
    compile.extendsFrom(provided)
}

dependencies {
    compile project(':core')
    compile project(':BotLib')
}

build {
    println 'Building ' + project.name

    ext {
        if (project.hasProperty('privateBuildDate')) {
            privateBuildDate = project.property('privateBuildDate')
        } else {
            privateBuildDate = new Date().format('yyMMddHHmm')
        }

        if (project.hasProperty('publicVersion')) {
            publicVersion = project.property('publicVersion')
        } else {
            if (minorVersion > 0) {
                publicVersion = new Date().format('yy.MM') + '.' + minorVersion
            } else {
                publicVersion = new Date().format('yy.MM')
            }
        }
    }

    jar {
        baseName = 'bot-manager'
        libsDirName = new File(rootProject.projectDir, "build/app")

        manifest {
            attributes 'Version': build.ext.publicVersion
            attributes 'Class-Path': 'corelib.jar botlib.jar'
            attributes 'Main-Class': 'net.rickiekarp.botter.MainApp'
            attributes 'Build-Time': build.ext.privateBuildDate
        }

        // Include all of the jars from the bundled configuration in our jar
        from configurations.provided.asFileTree.files.collect { zipTree(it) }
    }
}

//task modifyXmlNode() {
//    def xmlFile = "/home/rickie/git/appbox/test.xml"
//    def xml = new XmlParser().parse(xmlFile)
//
//    xml.module.each {
//        if(it.@id == 'App') {
//            println 'Updating version number'
//            it.@version = majorVersion + '.' + new Date().format('yyMMdd') + '.' + buildVersion
//        }
//    }
//
//    def nodePrinter = new XmlNodePrinter(new PrintWriter(new FileWriter(xmlFile)))
//    nodePrinter.preserveWhitespace = true
//    nodePrinter.print(xml)
//}