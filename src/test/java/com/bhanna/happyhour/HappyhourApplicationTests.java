package com.bhanna.happyhour;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.AssertionErrors;

@SpringBootTest
class HappyhourApplicationTests {

    @Test
    void contextLoads() {
        AssertionErrors.assertTrue("Application tests loading", true);
    }

}
