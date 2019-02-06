import org.jetbrains.dokka.gradle.DokkaTask

group = "com.github.pemistahl"
version = "0.4.0-SNAPSHOT"
description = "A natural language detection library for Kotlin and Java, suitable for long and short text alike"

val supportedDetectors: String by project
val supportedLanguages: String by project
val linguaMainClass: String by project
val csvHeader: String by project

plugins {
    kotlin("jvm") version "1.3.20"
    id("com.adarshr.test-logger") version "1.6.0"
    id("org.jetbrains.dokka") version "0.9.17"
    jacoco
}

jacoco.toolVersion = "0.8.3"

sourceSets {
    main {
        resources {
            //include("training-data/ca/*")
            exclude("training-data/**", "language-models/*/sixgrams.json")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform { failFast = true }
}

tasks.test {
    filter { includeTestsMatching("*Test") }
}

tasks.jacocoTestReport {
    dependsOn("test")
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.isEnabled = true
    }
}

tasks.register<Test>("accuracyReports") {
    val allowedDetectors = supportedDetectors.split(',')
    val detectors = if (project.hasProperty("detectors"))
        project.property("detectors").toString().split(Regex("\\s*,\\s*"))
    else allowedDetectors

    detectors.filterNot { it in allowedDetectors }.forEach {
        throw GradleException(
            """
            detector '$it' does not exist
            supported detectors: ${allowedDetectors.joinToString(", ")}
            """.trimIndent()
        )
    }

    val allowedLanguages = supportedLanguages.split(',')
    val languages = if (project.hasProperty("languages"))
        project.property("languages").toString().split(Regex("\\s*,\\s*"))
    else allowedLanguages

    languages.filterNot { it in allowedLanguages }.forEach {
        throw GradleException("language '$it' is not supported")
    }

    maxHeapSize = "3072m"
    reports.html.isEnabled = false
    reports.junitXml.isEnabled = false

    testlogger {
        showPassed = false
        showSkipped = false
    }

    filter {
        detectors.forEach { detector ->
            languages.forEach { language ->
                includeTestsMatching(
                    "com.github.pemistahl.lingua.report.${detector.toLowerCase()}.${language}DetectionAccuracyReport"
                )
            }
        }
    }
}

tasks.register("writeCsv") {
    val accuracyReportsDirectoryName = "accuracy-reports"
    val accuracyReportsDirectory = file(accuracyReportsDirectoryName)
    if (!accuracyReportsDirectory.exists()) {
        throw GradleException("directory '$accuracyReportsDirectoryName' does not exist")
    }

    val detectors = supportedDetectors.split(',')
    val languages = supportedLanguages.split(',').toMutableList()
    languages.removeAll("Bokmal,Nynorsk,Latin".split(','))

    val csvFile = file("$accuracyReportsDirectoryName/aggregated-accuracy-values.csv")
    val stringToSplitAt = ">> Exact values:"

    if (csvFile.exists()) csvFile.delete()
    csvFile.createNewFile()
    csvFile.appendText(csvHeader.replace(',', ' '))
    csvFile.appendText("\n")

    for (language in languages) {
        csvFile.appendText(language)
        csvFile.appendText(" ")

        for (detector in detectors) {
            val languageReportFileName = "$accuracyReportsDirectoryName/${detector.toLowerCase()}/$language.txt"
            val languageReportFile = file(languageReportFileName)
            if (!languageReportFile.exists()) {
                csvFile.delete()
                throw GradleException("file '$languageReportFileName' does not exist")
            }

            for (line in languageReportFile.readLines()) {
                if (line.startsWith(stringToSplitAt)) {
                    val accuracyValues = line.split(stringToSplitAt)[1].split(' ').slice(1..4).joinToString(" ")
                    csvFile.appendText(accuracyValues)
                    csvFile.appendText(" ")
                }
            }
        }

        csvFile.appendText("\n")
    }

    println("file 'aggregated-accuracy-values.csv' was written successfully")
}

tasks.withType<DokkaTask> {
    jdkVersion = 6
    reportUndocumented = false
}

tasks.register<DokkaTask>("dokkaJavadoc") {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/dokkaJavadoc"
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn("dokkaJavadoc")
    classifier = "javadoc"
    from("$buildDir/dokkaJavadoc")
}

tasks.register<Jar>("sourcesJar") {
    classifier = "sources"
    from("src/main/kotlin")
}

tasks.register<Jar>("jarWithDependencies") {
    classifier = "with-dependencies"
    dependsOn(configurations.runtimeClasspath)
    from(sourceSets.main.get().output)
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    })
    manifest { attributes("Main-Class" to linguaMainClass) }
    exclude("META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.SF")
}

tasks.register<JavaExec>("startRepl") {
    main = linguaMainClass
    standardInput = System.`in`
    classpath = sourceSets["main"].runtimeClasspath
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.mapdb:mapdb:3.0.7") {
        exclude("org.jetbrains.kotlin:kotlin-stdlib")
    }

    val junitVersion = "5.3.2"

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("io.mockk:mockk:1.9")
    //testImplementation("org.apache.commons:commons-csv:1.6")

    testImplementation("com.optimaize.languagedetector:language-detector:0.6")
    testImplementation("org.apache.tika:tika-langdetect:1.20")

    val slf4jVersion = "1.7.25"

    testImplementation("org.slf4j:slf4j-api:$slf4jVersion")
    testImplementation("org.slf4j:slf4j-log4j12:$slf4jVersion")
}

repositories {
    jcenter()
}
