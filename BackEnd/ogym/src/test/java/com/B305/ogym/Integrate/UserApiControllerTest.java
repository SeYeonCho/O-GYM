//package com.B305.ogym.Integrate;
//
//import com.B305.ogym.controller.UserApiController;
//import com.B305.ogym.domain.autority.Authority;
//import com.B305.ogym.domain.autority.AuthorityRepository;
//import com.B305.ogym.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@AutoConfigureMockMvc
//class UserApiControllerTest {
//
//    @Autowired
//    private UserApiController userApiController;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AuthorityRepository authorityRepository;
//
//    @Autowired
//    private MockMvc mvc;
//
//
//
//    @BeforeEach
//    public void init() {
//        authorityRepository.save(new Authority("ROLE_USER"));
//
//    }
//
//    @DisplayName("회원가입시 잘못된 입력값 테스트")
//    @Test
//    public void signupByWrongInputTest() throws Exception{
//        //given
////        final UserDto userDto = UserDto.builder()
////                                    .email("email")
////                                    .password("password")
////                                    .nickname("nickname")
////                                    .build();
////        System.out.println(userDto);
////        //when
////
////        final ResultActions actions = mvc
////            .perform( MockMvcRequestBuilders.post("/api/signup")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(new Gson().toJson(userDto))
////            );
////
////        //then
////        final MvcResult mvcResult = actions.andExpect(
////            org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk()).andReturn();
////        final String token = mvcResult.getResponse().getContentAsString();
////        System.out.println(token);
//
//    }
//}