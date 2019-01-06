package com.github.pemistahl.lingua.report.optimaize

import com.github.pemistahl.lingua.report.AbstractDutchDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.OPTIMAIZE
import org.junit.jupiter.api.AfterAll

class DutchDetectionAccuracyReport : AbstractDutchDetectionAccuracyReport(OPTIMAIZE) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
