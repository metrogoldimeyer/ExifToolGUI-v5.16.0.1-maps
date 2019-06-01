import java.text.SimpleDateFormat
import java.util.Date

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
}

project.ext {
    set("mainClassName", "org.hvdw.jexiftoolgui.mainScreen")
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://jetbrains.bintray.com/intellij-third-party-dependencies")
}


dependencies {


    // Add dependencies of forms
    compile(group = "com.intellij", name = "forms_rt", version = "7.0.3")
    testImplementation(group = "junit", name = "junit", version = "4.12")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

}


val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = project.ext["mainClassName"] as String
        attributes["Build-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date())
        attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
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


