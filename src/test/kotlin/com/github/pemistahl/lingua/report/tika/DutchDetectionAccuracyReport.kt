package com.github.pemistahl.lingua.report.tika

import com.github.pemistahl.lingua.report.AbstractDutchDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.TIKA
import org.junit.jupiter.api.AfterAll

class DutchDetectionAccuracyReport : AbstractDutchDetectionAccuracyReport(TIKA) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
