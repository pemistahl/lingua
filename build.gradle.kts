/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
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
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import ru.vyarus.gradle.plugin.python.task.PythonTask
import java.util.Locale

val linguaTaskGroup: String by project
val linguaGroupId: String by project
val linguaArtifactId: String by project
val linguaVersion: String by project
val linguaName: String by project
val linguaDescription: String by project
val linguaLicenseName: String by project
val linguaLicenseUrl: String by project
val linguaWebsiteUrl: String by project
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
val githubPackagesUrl: String by project

val compileTestKotlin: KotlinCompile by tasks

group = linguaGroupId
version = linguaVersion
description = linguaDescription

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("com.adarshr.test-logger") version "3.1.0"
    id("com.asarkar.gradle.build-time-tracker") version "3.0.1"
    id("org.jetbrains.dokka") version "1.6.0"
    id("ru.vyarus.use-python") version "2.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    `maven-publish`
    signing
    jacoco
}

jacoco.toolVersion = "0.8.7"

sourceSets {
    main {
        resources {
            exclude("training-data/**")
        }
    }
    create("accuracyReport") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val accuracyReportImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}

configurations["accuracyReportRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

compileTestKotlin.kotlinOptions.jvmTarget = "1.8"

tasks.named("compileAccuracyReportKotlin", KotlinCompile::class) {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform { failFast = true }
}

tasks.test {
    maxParallelForks = 1
}

tasks.jacocoTestReport {
    dependsOn("test")
    reports {
        xml.isEnabled = true
        csv.isEnabled = false
        html.isEnabled = true
    }
}

tasks.register<Test>("accuracyReport") {
    group = linguaTaskGroup
    description = "Runs Lingua on provided test data, and writes detection accuracy reports for each language."
    testClassesDirs = sourceSets["accuracyReport"].output.classesDirs
    classpath = sourceSets["accuracyReport"].runtimeClasspath

    val allowedDetectors = linguaSupportedDetectors.split(',')
    val detectors = if (project.hasProperty("detectors"))
        project.property("detectors").toString().split(Regex("\\s*,\\s*"))
    else allowedDetectors

    detectors.filterNot { it in allowedDetectors }.forEach {
        throw GradleException(
            """
            detector '$it' does not exist
            supported detectors: ${allowedDetectors.joinToString(
                ", "
            )}
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
                    "$linguaGroupId.$linguaArtifactId.report" +
                        ".${detector.toLowerCase(Locale.ROOT)}.${language}DetectionAccuracyReport"
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
                val languageReportFileName =
                    "$accuracyReportsDirectoryName/${detector.toLowerCase(Locale.ROOT)}/$language.txt"
                val languageReportFile = file(languageReportFileName)

                if (languageReportFile.exists()) {
                    for (line in languageReportFile.readLines()) {
                        if (line.startsWith(stringToSplitAt)) {
                            val accuracyValues = line
                                .split(stringToSplitAt)[1]
                                .split(' ')
                                .slice(1..4)
                                .joinToString(",")
                            csvFile.appendText(",")
                            csvFile.appendText(accuracyValues)
                        }
                    }
                } else {
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

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        jdkVersion.set(6)
        reportUndocumented.set(false)
        perPackageOption {
            matchingRegex.set(".*\\.(app|internal).*")
            suppress.set(true)
        }
    }
}

tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn("dokkaJavadoc")
    group = "Build"
    description = "Assembles a jar archive containing Javadoc documentation."
    classifier = "javadoc"
    from("$buildDir/dokka/javadoc")
}

tasks.register<Jar>("sourcesJar") {
    group = "Build"
    description = "Assembles a jar archive containing the main source code."
    classifier = "sources"
    from("src/main/kotlin")
}

tasks.register<ConfigureShadowRelocation>("relocateDependencies") {
    group = "shadow"
    description = "Specifies the ShadowJar task for which to relocate the dependencies."
    target = tasks["jarWithDependencies"] as ShadowJar
}

tasks.register<ShadowJar>("jarWithDependencies") {
    dependsOn("relocateDependencies")
    group = "Build"
    description = "Assembles a jar archive containing the main classes and all external dependencies."
    classifier = "with-dependencies"
    from(sourceSets.main.get().output)
    configurations = listOf(project.configurations.runtimeClasspath.get())
    manifest { attributes("Main-Class" to linguaMainClass) }
}

tasks.register<JavaExec>("runLinguaOnConsole") {
    group = linguaTaskGroup
    description = "Starts a REPL (read-evaluate-print loop) to try Lingua on the command line."
    main = linguaMainClass
    standardInput = System.`in`
    classpath = sourceSets["main"].runtimeClasspath
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("io.mockk:mockk:1.12.1")

    accuracyReportImplementation("com.optimaize.languagedetector:language-detector:0.6")
    accuracyReportImplementation("org.apache.opennlp:opennlp-tools:1.9.4")
    accuracyReportImplementation("org.apache.tika:tika-core:2.1.0")
    accuracyReportImplementation("org.apache.tika:tika-langdetect-optimaize:2.1.0")
    accuracyReportImplementation("org.slf4j:slf4j-nop:1.7.32")
}

python {
    pip("matplotlib:3.4.1")
    pip("seaborn:0.11.1")
    pip("pandas:1.2.4")
    pip("numpy:1.20.0")
}

publishing {
    publications {
        create<MavenPublication>("lingua") {
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

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(githubPackagesUrl)
            credentials {
                username = linguaDeveloperId
                password = project.findProperty("ghPackagesToken") as String?
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype()
    }
}

signing {
    sign(publishing.publications["lingua"])
}

repositories {
    mavenCentral()
}
