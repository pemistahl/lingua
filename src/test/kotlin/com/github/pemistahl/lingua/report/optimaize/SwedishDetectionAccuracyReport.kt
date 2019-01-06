package com.github.pemistahl.lingua.report.optimaize

import com.github.pemistahl.lingua.report.AbstractSwedishDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.OPTIMAIZE
import org.junit.jupiter.api.AfterAll

class SwedishDetectionAccuracyReport : AbstractSwedishDetectionAccuracyReport(OPTIMAIZE) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
