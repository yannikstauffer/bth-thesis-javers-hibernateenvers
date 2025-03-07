package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class ResourceUtilsTest {

    public static final String TESTING_FILE = "testingfile.yaml";
    static final String TESTING_FILE_COPY = "copy.yaml";

    @BeforeEach
    void setUp() {
        ResourceUtils.delete(TESTING_FILE_COPY);
    }

    @AfterEach
    void tearDown() {
        ResourceUtils.delete(TESTING_FILE_COPY);

        if(!ResourceUtils.fileExists(ResourceUtilsTest.TESTING_FILE)) {
            fail("You have deleted the testing file. That was not supposed to happen.");
        }
    }

    @Test
    void copy() {
        ResourceUtils.copy(TESTING_FILE, TESTING_FILE_COPY);
        assertThat(new ClassPathResource(TESTING_FILE_COPY).exists()).isTrue();
        assertThat(new ClassPathResource(TESTING_FILE).exists()).isTrue();
    }
}
