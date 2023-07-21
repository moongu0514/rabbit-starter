package hsja.ibas.rabbit.producer.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DemoListenerTest {

    private DemoListener demoListenerUnderTest;

    @BeforeEach
    void setUp() {
        demoListenerUnderTest = new DemoListener();
    }

    @Test
    void testGetNextDay() {
        assertThat(demoListenerUnderTest.getNextDay("2023-07-07")).isEqualTo("2023-07-07 23:59:59");
    }
}
