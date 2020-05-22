/*
 * Copyright © 2018-2020 Peter M. Stahl pemistahl@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ru.vyarus.gradle.plugin.python.task.PythonTask

val linguaTaskGroup: String by project
val linguaGroupId: String by project
val linguaArtifactId: String by project
val linguaVersion: String by project
val linguaName: String by project
val linguaDescription: String by project
val linguaLicenseId: String by project
val linguaLicenseName: String by project
val linguaLicenseUrl: String by project
val linguaRepositoryName: String by project
val linguaWebsiteUrl: String by project
val linguaVcsUrl: String by project
val linguaIssueTrackerUrl: String by project
val linguaDeveloperId: String by project
val linguaDeveloperName: String by project
val linguaDeveloperEmail: String by project
val linguaDeveloperUrl: String by project
val linguaScmConnection: String by project
val linguaScmDeveloperConnection: String by project
val linguaScmUrl: String by project
val linguaSupportedDetectors: String by project
val linguaSupportedLanguages: String by project
val linguaMainClass: String by project
val linguaCsvHeader: String by project

val compileTestKotlin: KotlinCompile by tasks

group = linguaGroupId
version = linguaVersion
description = linguaDescription

plugins {
    kotlin("jvm") version "1.3.72"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.72"
    id("com.adarshr.test-logger") version "2.0.0"
    id("org.jetbrains.dokka") version "0.10.0"
    id("ru.vyarus.use-python") version "1.2.0"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    jacoco
}

jacoco.toolVersion = "0.8.4"

sourceSets {
    main {
        resources {
            exclude("training-data/**")
        }
    }
}

compileTestKotlin.kotlinOptions.jvmTarget = "1.8"

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

tasks.register<Test>("writeAccuracyReports") {
    group = linguaTaskGroup
    description = "Runs Lingua on provided test data, reports detection accuracy for each language and writes results to files."

    val allowedDetectors = linguaSupportedDetectors.split(',')
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

    val allowedLanguages = linguaSupportedLanguages.split(',')
    val languages = if (project.hasProperty("languages"))
        project.property("languages").toString().split(Regex("\\s*,\\s*"))
    else allowedLanguages

    languages.filterNot { it in allowedLanguages }.forEach {
        throw GradleException("language '$it' is not supported")
    }

    val availableCpuCores = Runtime.getRuntime().availableProcessors()
    val cpuCoresRepr = if (project.hasProperty("cpuCores"))
        project.property("cpuCores").toString()
    else "1"

    val cpuCores = try {
        cpuCoresRepr.toInt()
    } catch (e: NumberFormatException) {
        throw GradleException("'$cpuCoresRepr' is not a valid value for argument -PcpuCores")
    }

    if (cpuCores !in 1..availableCpuCores) {
        throw GradleException(
            """
            $cpuCores cpu cores are not supported
            minimum: 1
            maximum: $availableCpuCores
            """.trimIndent()
        )
    }

    maxHeapSize = "4096m"
    maxParallelForks = cpuCores
    reports.html.isEnabled = false
    reports.junitXml.isEnabled = false

    testlogger {
        theme = ThemeType.STANDARD_PARALLEL
        showPassed = false
        showSkipped = false
    }

    filter {
        detectors.forEach { detector ->
            languages.forEach { language ->
                includeTestsMatching(
                    "$linguaGroupId.$linguaArtifactId.report.${detector.toLowerCase()}.${language}DetectionAccuracyReport"
                )
            }
        }
    }
}

tasks.register("writeAggregatedAccuracyReport") {
    group = linguaTaskGroup
    description = "Creates a table from all accuracy detection reports and writes it to a CSV file."

    doLast {
        val accuracyReportsDirectoryName = "accuracy-reports"
        val accuracyReportsDirectory = file(accuracyReportsDirectoryName)
        if (!accuracyReportsDirectory.exists()) {
            throw GradleException("directory '$accuracyReportsDirectoryName' does not exist")
        }

        val detectors = linguaSupportedDetectors.split(',')
        val languages = linguaSupportedLanguages.split(',')

        val csvFile = file("$accuracyReportsDirectoryName/aggregated-accuracy-values.csv")
        val stringToSplitAt = ">> Exact values:"

        if (csvFile.exists()) csvFile.delete()
        csvFile.createNewFile()
        csvFile.appendText(linguaCsvHeader)
        csvFile.appendText("\n")

        for (language in languages) {
            csvFile.appendText(language)

            for (detector in detectors) {
                val languageReportFileName = "$accuracyReportsDirectoryName/${detector.toLowerCase()}/$language.txt"
                val languageReportFile = file(languageReportFileName)

                if (languageReportFile.exists()) {
                    for (line in languageReportFile.readLines()) {
                        if (line.startsWith(stringToSplitAt)) {
                            val accuracyValues = line.split(stringToSplitAt)[1].split(' ').slice(1..4).joinToString(",")
                            csvFile.appendText(",")
                            csvFile.appendText(accuracyValues)
                        }
                    }
                }
                else {
                    csvFile.appendText(",NaN,NaN,NaN,NaN")
                }
            }

            csvFile.appendText("\n")
        }

        println("file 'aggregated-accuracy-values.csv' written successfully")
    }
}

tasks.register<PythonTask>("drawAccuracyPlots") {
    dependsOn("writeAggregatedAccuracyReport")
    group = linguaTaskGroup
    description = "Draws plots showing the results of the accuracy detection reports."
    command = "src/python-scripts/draw_accuracy_plots.py"
}

tasks.register<PythonTask>("writeAccuracyTable") {
    dependsOn("writeAggregatedAccuracyReport")
    group = linguaTaskGroup
    description = "Creates HTML table from all accuracy detection results and writes it to a markdown file."
    command = "src/python-scripts/write_accuracy_table.py"
}

tasks.withType<DokkaTask> {
    configuration {
        jdkVersion = 6
        reportUndocumented = false
        perPackageOption {
            prefix = "com.github.pemistahl.lingua.app"
            suppress = true
        }

        perPackageOption {
            prefix = "com.github.pemistahl.lingua.internal"
            suppress = true
        }
    }
}

tasks.register<DokkaTask>("dokkaJavadoc") {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/dokkaJavadoc"
}

tasks.register<Jar>("dokkaJavadocJar") {
    group = "Build"
    description = "Assembles a jar archive containing Javadoc documentation."
    dependsOn("dokkaJavadoc")
    classifier = "javadoc"
    from("$buildDir/dokkaJavadoc")
}

tasks.register<Jar>("sourcesJar") {
    group = "Build"
    description = "Assembles a jar archive containing the main source code."
    classifier = "sources"
    from("src/main/kotlin")
}

tasks.register<Jar>("jarWithDependencies") {
    group = "Build"
    description = "Assembles a jar archive containing the main classes and all external dependencies."
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

tasks.register<JavaExec>("runLinguaOnConsole") {
    group = linguaTaskGroup
    description = "Starts a REPL (read-evaluate-print loop) to try Lingua on the command line."
    main = linguaMainClass
    standardInput = System.`in`
    classpath = sourceSets["main"].runtimeClasspath
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
    testImplementation("org.assertj:assertj-core:3.14.0")
    testImplementation("io.mockk:mockk:1.9.3")

    testImplementation("com.optimaize.languagedetector:language-detector:0.6")
    testImplementation("org.apache.opennlp:opennlp-tools:1.9.1")
    testImplementation("org.apache.tika:tika-langdetect:1.23")

    val slf4jVersion = "1.7.25"

    testImplementation("org.slf4j:slf4j-api:$slf4jVersion")
    testImplementation("org.slf4j:slf4j-log4j12:$slf4jVersion")
}

python {
    pip("matplotlib:3.1.1")
    pip("seaborn:0.9.0")
    pip("pandas:0.25.3")
    pip("numpy:1.17.0")
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("linguaPublication")

    dryRun = false
    publish = true

    with(pkg) {
        repo = linguaRepositoryName
        name = linguaArtifactId
        desc = linguaDescription
        websiteUrl = linguaWebsiteUrl
        issueTrackerUrl = linguaIssueTrackerUrl
        vcsUrl = linguaVcsUrl

        setLicenses(linguaLicenseId)

        with(version) {
            name = linguaVersion
            desc = linguaDescription
            vcsTag = "v$linguaVersion"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("linguaPublication") {
            groupId = linguaGroupId
            artifactId = linguaArtifactId
            version = linguaVersion

            from(components["kotlin"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["jarWithDependencies"])
            artifact(tasks["dokkaJavadocJar"])

            pom {
                name.set(linguaName)
                description.set(linguaDescription)
                url.set(linguaWebsiteUrl)

                licenses {
                    license {
                        name.set(linguaLicenseName)
                        url.set(linguaLicenseUrl)
                    }
                }
                developers {
                    developer {
                        id.set(linguaDeveloperId)
                        name.set(linguaDeveloperName)
                        email.set(linguaDeveloperEmail)
                        url.set(linguaDeveloperUrl)
                    }
                }
                scm {
                    connection.set(linguaScmConnection)
                    developerConnection.set(linguaScmDeveloperConnection)
                    url.set(linguaScmUrl)
                }
            }
        }
    }
}

repositories {
    jcenter()
}
