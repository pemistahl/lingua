package test;

import com.github.pemistahl.lingua.api.*;
import org.junit.jupiter.api.Test;

import static com.github.pemistahl.lingua.api.Language.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests basic Lingua functionality. The main purpose of this test is to verify that the
 * packages can be accessed from a different module and that Lingua specifies all required
 * modules and can be used successfully.
 */
class LinguaTest {
    @Test
    void test() {
        LanguageDetector detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH, GERMAN, SPANISH).build();
        Language detectedLanguage = detector.detectLanguageOf("languages are awesome");
        assertEquals(ENGLISH, detectedLanguage);
    }
}
