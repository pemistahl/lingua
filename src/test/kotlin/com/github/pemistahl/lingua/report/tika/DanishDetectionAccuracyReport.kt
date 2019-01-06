package com.github.pemistahl.lingua.report.tika

import com.github.pemistahl.lingua.report.AbstractDanishDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.TIKA
import org.junit.jupiter.api.AfterAll

class DanishDetectionAccuracyReport : AbstractDanishDetectionAccuracyReport(TIKA) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
