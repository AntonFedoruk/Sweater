package ua.antonfedoruk.sweater;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ua.antonfedoruk.sweater.controller.MessageController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// The @SpringBootTest annotation tells Spring Boot to look for a main configuration class (one with @SpringBootApplication,
// for instance) and use that to start a Spring application context.
@SpringBootTest
// Another useful approach is to not start the server at all but to test only the layer below that, where Spring handles the incoming HTTP
// request and hands it off to your controller. That way, almost of the full stack is used, and your code will be called in exactly the same
// way as if it were processing a real HTTP request but without the cost of starting the server. To do that, use Spring’s MockMvc and ask for
// that to be injected for you by using the @AutoConfigureMockMvc annotation on the test case.
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")  //is a class-level annotation that you can use to configure the locations of properties files and inlined properties to be added to the set of PropertySources in the Environment for an ApplicationContext loaded for an integration test
public class LoginTest {
    //Spring interprets the @Autowired annotation, and the controller is injected before the test methods are run.
    @Autowired
    private MessageController messageController;

    @Autowired
    private MockMvc mockMvc;

    // simple sanity check test that will fail if the application context cannot start.
    @Test // @Test annotation notices all tests methods from test case class(this class)
    public void contextLoads() throws Exception {
        //To convince yourself that the context is creating your controller, you could add an assertion
        assertThat(messageController).isNotNull(); //We use AssertJ (which provides assertThat() and other methods) to express the test assertions.
    }

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/")) //we do GET request on mail page of our project
                .andDo(print())  // display result to console; check for correctness
                .andExpect(status().isOk()) // обгортка assertThat; we expect that in response status code HTTP will be 200
                .andExpect(content().string(containsString("Hello, guest"))) // we expect that response contain specific string
                .andExpect(content().string(containsString("Please, login")));
    }

    //authentication test
    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())// we expect redirection to login
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @Sql(value = "/create-user-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //SQL script that should be executed before method invocation
    @Sql(value = "/clear-user-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) //SQL script that should be executed after method invocation
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(formLogin().user("user1").password("1")) // formLogin() look at how we defined login page in context and make request to this page
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messages"));

    }

    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/login").param("user", "user100"))
                .andDo(print())
                .andExpect(status().isForbidden()); //status code 403
    }
}