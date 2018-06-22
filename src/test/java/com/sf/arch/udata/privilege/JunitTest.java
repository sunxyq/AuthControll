package com.sf.arch.udata.privilege;

import com.sf.arch.udata.privilege.controller.PrivilegeController;
import com.sf.arch.udata.privilege.service.PrivilegeService;
import com.sf.arch.udata.privilege.service.PrivilegeServiceImpl;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UdataPrivilegeApplication.class)
@WebAppConfiguration
@ComponentScan(basePackages={"com.sf.arch.udata.privilege"})
public class JunitTest {

    private MockMvc mockMvc; // 模拟MVC对象，通过MockMvcBuilders.webAppContextSetup(this.wac).build()初始化。

    @Autowired
    private WebApplicationContext wac; // 注入WebApplicationContext

    @Autowired
    private PrivilegeService service;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public  void testIsEmpty(){
        System.out.println(service.getMaxId());
    }

    @Test
    public void Test() throws  Exception{

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/test")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("a","11")
                .param("b","11")
                .param("c","11").accept(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();

        System.out.println( status + ":" + content );
    }
}
