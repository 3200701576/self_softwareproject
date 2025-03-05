package group6.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest  // 这个注解会加载 Spring Boot 上下文，测试应用是否能正常运行
class BackendApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);  // 仅测试 Spring Boot 是否能正常启动
    }
}
