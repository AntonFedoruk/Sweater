package ua.antonfedoruk.sweater;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ua.antonfedoruk.sweater.controller.MessageController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "user1") //this annotation provide user for authentication
@TestPropertySource("/application-test.properties")
//is a class-level annotation that you can use to configure the locations of properties files and inlined properties to be added to the set of PropertySources in the Environment for an ApplicationContext loaded for an integration test
@Sql(value = {"/create-user-before.sql", "/messages-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//SQL script that should be executed before method invocation
@Sql(value = {"/messages-list-after.sql", "/clear-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//SQL script that should be executed after method invocation
public class MessageControllerTest {
    @Autowired
    private MessageController messageController;

    @Autowired
    private MockMvc mockMvc;

    //     !!! before authentication test, user must be authenticated -> for this purpose @WithUserDetails  annotation used with class !!!
    @Test
    public void messagesPageTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(authenticated()) //test will pass only if in context current user has got web-session
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div").string("user1")); //we expect "user1" string at xpath() (XPath - query language for xml documents)
    }

    @Test
    public void messageListTest() throws Exception {
        this.mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='message-list']/div").nodeCount(4)); //we expect that we will obtain certain count of node on page (in our sql scripts we have 4 messages, so count is 4)
    }

    @Test
    public void filterMessageTest() throws Exception {
        this.mockMvc.perform(get("/messages").param("filter", "my-tag"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='message-list']/div").nodeCount(2)) //we expect that we will obtain certain count of node on page with "my-tag" tag (in our sql scripts we have 4 messages, two of which have got "my-tag" tag, so count is 2)
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=1]").exists()) //we expect to obtain message that will have data-id attribute equal to 1
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=3]").exists());
    }

    @Test
    public void addMessageToListTest() throws Exception {
        //we should use multipart(), rather than post() because we use MultipartFile in  post request (in add() in MainController)
        MockHttpServletRequestBuilder multipart = multipart("/messages")
                .file("file", "123".getBytes())
                .param("text", "fifth")  //one of necessary fields of Message.class
                .param("tag", "new one")
                .with(csrf());;

        this.mockMvc.perform(multipart)
        .andDo(print())
        .andExpect(authenticated())
                // also after adding, new total amount of messages should be equal to 5
                .andExpect(xpath("//div[@id='message-list']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=10]").exists())
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=10]/div/h5").string("new one"))
                .andExpect(xpath("//div[@id='message-list']/div[@data-id=10]/div/p").string("fifth"));
    }
}

