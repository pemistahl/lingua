package com.github.pemistahl.lingua.report.optimaize

import com.github.pemistahl.lingua.report.AbstractDanishDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.OPTIMAIZE
import org.junit.jupiter.api.AfterAll

class DanishDetectionAccuracyReport : AbstractDanishDetectionAccuracyReport(OPTIMAIZE) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
