package com.github.pemistahl.lingua.report.tika

import com.github.pemistahl.lingua.report.AbstractSwedishDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.TIKA
import org.junit.jupiter.api.AfterAll

class SwedishDetectionAccuracyReport : AbstractSwedishDetectionAccuracyReport(TIKA) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
