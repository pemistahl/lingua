/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.io.TempDir;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.github.pemistahl.lingua.api.io.LanguageModelFilesWriter;

import static java.util.stream.Collectors.toList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import static com.github.pemistahl.lingua.api.Language.ENGLISH;
import static com.github.pemistahl.lingua.api.Language.FRENCH;
import static com.github.pemistahl.lingua.api.Language.SPANISH;

/**
 * Tests basic Lingua functionality. The main purpose of this test is to verify that the
 * packages can be accessed from a different module and that Lingua specifies all required
 * modules and can be used successfully.
 */
class LinguaTest {
    @Test
    void testDetector() {
        LanguageDetector detector = LanguageDetectorBuilder.fromLanguages(ENGLISH, FRENCH, SPANISH).build();
        Language detectedLanguage = detector.detectLanguageOf("languages are awesome");
        assertEquals(ENGLISH, detectedLanguage);
    }

    @Test
    @DisabledOnOs(WINDOWS) // TempDir cannot be deleted on Windows
    void testLanguageModelFilesWriter(@TempDir Path outputDirectoryPath) throws Exception {
        Path inputFilePath = Files.createTempFile(null, null);

        LanguageModelFilesWriter.createAndWriteLanguageModelFiles(
            inputFilePath,
            StandardCharsets.UTF_8,
            outputDirectoryPath,
            ENGLISH,
            "\\p{L}&&\\p{IsLatin}"
        );

        List<Path> filesList = Files.list(outputDirectoryPath).collect(toList());

        assertEquals(5, filesList.size());

        inputFilePath.toFile().delete();
    }
}
