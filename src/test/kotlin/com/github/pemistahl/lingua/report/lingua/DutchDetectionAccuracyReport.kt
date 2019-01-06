package com.github.pemistahl.lingua.report.lingua

import com.github.pemistahl.lingua.report.AbstractDutchDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.LINGUA
import org.junit.jupiter.api.AfterAll

class DutchDetectionAccuracyReport : AbstractDutchDetectionAccuracyReport(LINGUA) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
