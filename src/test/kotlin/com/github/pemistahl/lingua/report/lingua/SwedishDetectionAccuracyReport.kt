package com.github.pemistahl.lingua.report.lingua

import com.github.pemistahl.lingua.report.AbstractSwedishDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.LINGUA
import org.junit.jupiter.api.AfterAll

class SwedishDetectionAccuracyReport : AbstractSwedishDetectionAccuracyReport(LINGUA) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
