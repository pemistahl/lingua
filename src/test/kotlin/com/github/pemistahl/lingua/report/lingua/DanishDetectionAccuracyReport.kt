package com.github.pemistahl.lingua.report.lingua

import com.github.pemistahl.lingua.report.AbstractDanishDetectionAccuracyReport
import com.github.pemistahl.lingua.report.LanguageDetectorImplementation.LINGUA
import org.junit.jupiter.api.AfterAll

class DanishDetectionAccuracyReport : AbstractDanishDetectionAccuracyReport(LINGUA) {

    @AfterAll
    fun afterAll() = logger.info(statisticsReport())
}
