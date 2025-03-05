package group6.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import group6.demo.dto.UserLoginDTO;
import group6.demo.dto.UserRegistrationDTO;
import group6.demo.entity.User;
import group6.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController; // 添加导入


import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@RestController // 添加注解
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Autowired
    private ObjectMapper objectMapper; // 用于 JSON 转换

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLoginUser_Success() throws Exception {
        // 模拟输入数据
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername("testUser");
        loginDTO.setPassword("Password123");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setUserType(1); // 确保 setUserType 方法接受 String 类型参数
        mockUser.setEmail("test@example.com");

        when(userService.loginUser(any(UserLoginDTO.class))).thenReturn(mockUser);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Login successful");
        expectedResponse.put("userId", 1L);
        expectedResponse.put("username", "testUser");
        expectedResponse.put("userType", "admin");
        expectedResponse.put("email", "test@example.com");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("newUser");
        registrationDTO.setPassword("newPass123");
        registrationDTO.setEmail("newuser@example.com");
        registrationDTO.setMobile("17733103310");
        registrationDTO.setPassword("Software123");
        Date birthday = Date.valueOf("1990-01-01");
        registrationDTO.setBirthday(birthday);

        User registeredUser = new User();
        registeredUser.setId(2L);
        registeredUser.setUsername("newUser");

        when(userService.isEmailExists("newuser@example.com")).thenReturn(false);
        when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(registeredUser);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.username").value("newUser"));
    }

}