package com.jhj.action.app;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * 说明： JUnit测试action时使用的基类
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp/web.xml")
@ContextConfiguration(locations = {
	    "classpath*:spring.xml",
	    "classpath*:spring-mvc.xml",
	    "classpath*:spring-mybatis.xml"})




public class JUnitActionBase {

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    protected final String mediaType = "application/json;";

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
     }
}