import java.text.SimpleDateFormat
import java.util.Date
import org.gradle.jvm.tasks.Jar

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java project to get you started.
 * For more details take a look at the Java Quickstart chapter in the Gradle
 * User Manual available at https://docs.gradle.org/5.4.1/userguide/tutorial_java_projects.html
 */

plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the idea plugin, used for form builder
    idea
    // Apply the application plugin to add support for building an application
    application

    //Shadow plugin for fatJar
    id("com.github.johnrengelman.shadow") version ("6.1.0")
}

project.ext {
    set("mainClassName", "org.hvdw.jexiftoolgui.Application")
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://jetbrains.bintray.com/intellij-third-party-dependencies")

}


dependencies {

    //// Log4J and SLF4J configuration dependencies.
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.32")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.32")
    implementation(files("$projectDir/libs/logback-core-1.2.7.jar"))
    implementation(files("$projectDir/libs/logback-classic-1.2.7.jar"))

    // Add minimal json (35Kb), to be used for Nominatim queries and other json activities
    implementation(group = "com.eclipsesource.minimal-json", name = "minimal-json", version = "0.9.5")

    // Add Apache Commons Codec
    implementation(group = "commons-codec", name = "commons-codec", version = "1.15")

    // Add dependencies of forms
    implementation(group = "com.intellij", name = "forms_rt", version = "7.0.3")

    // Missing helpers and common utils in java
    implementation(group="org.apache.commons", name="commons-lang3", version="3.11")

    // At some time maybe when I feel like adding charts to display ISOs, focal length, aperture and the like
    //implementation(group = "org.jfree", name = "jfreechart", version = "1.5.0")
    // Or use Xchart: https://github.com/knowm/XChart    https://knowm.org/open-source/xchart/
    //implementation(group = "org.knowm.xchart", name = "xchart", version = "3.6.5")


/////////////////////////////////////////////////////
    // Drew Noakes  metadata extractor
    // https://github.com/drewnoakes/metadata-extractor
    //implementation(group="com.drewnoakes", name="metadata-extractor", version="2.16.0")


/////////////////////////////////////////////////
    // This is the itext pdf "community" library released under AGPL
    //implementation(group = "com.itextpdf", name = "itext7-core", version="7.1.12")
    implementation(files("$projectDir/libs/kernel-7.1.12.jar"))
    implementation(files("$projectDir/libs/io-7.1.12.jar"))
    implementation(files("$projectDir/libs/layout-7.1.12.jar"))
    implementation(files("$projectDir/libs/pdfa-7.1.12.jar"))
//////////////////////////////////////////////////
    // sqlite
    //implementation(group = "org.xerial", name = "sqlite-jdbc", version = "3.32.3")
    implementation(files("$projectDir/libs/sqlite-jdbc-3.36.0.3.jar"))


    // The opencsv library jar
    implementation(files("$projectDir/libs/opencsv-5.5.2.jar"))

    //jxmapviewer2 https://github.com/msteiger/jxmapviewer2
    implementation(group = "org.jxmapviewer", name = "jxmapviewer2", version = "2.6")


    // Testing dependencies
    testImplementation(group = "junit", name = "junit", version = "4.12")
    testImplementation(group = "com.github.stefanbirkner", name = "system-rules", version = "1.19.0")
    testImplementation(files("$projectDir/libs/logback-core-1.2.7.jar"))
    testImplementation(files("$projectDir/libs/logback-classic-1.2.7.jar"))

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

}

// New fatJar Gradle/kotlin way
val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Implementation-Title"] = "Gradle Jar File Example"
        //attributes["Implementation-Version"] = version
        attributes["Main-Class"] = project.ext["mainClassName"] as String
        attributes["Build-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date())
        attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
        attributes["Author"] = "Harry van der Wolf"
        attributes["SplashScreen-Image"] = "icons/jexiftoolgui-splashlogo.png"
        attributes["Multi-Release"] = "true"
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
//    doLast {
//        def servicesDir = file(
//    }
}

//tasks.wrapper {
//    distributionType = Wrapper.DistributionType.BIN
//}

tasks {
    "build" {
        dependsOn(fatJar)
    }
}

//task copyServices(type: Copy) {
//    from 'TwelveMonkeys/META-INF/services/'
//    include '*'
//    into 'build/tmp/fatJar'
//}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = project.ext["mainClassName"] as String
        attributes["Build-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date())
        attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
        attributes["Author"] = "Harry van der Wolf"
        attributes["SplashScreen-Image"] = "icons/jexiftoolgui-splashlogo.png"
        attributes["Multi-Release"] = "true"
    }
}

application {
    // Define the main class for the application
    mainClassName = project.ext["mainClassName"] as String
}


idea {
    module {
        outputDir = file("build/resources/main")
        testOutputDir = file("build/resources/test")
    }
}


