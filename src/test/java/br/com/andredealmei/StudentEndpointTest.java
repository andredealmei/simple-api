package br.com.andredealmei;

import br.com.andredealmei.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {

            return new RestTemplateBuilder().basicAuthorization("andre", "123");

        }

    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are incorrect */
    public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        TestRestTemplate template = restTemplate.withBasicAuth("123", "123");
        ResponseEntity<String> responsy = template.getForEntity("/v1/protected/students/",
                String.class);
        assertThat(responsy.getStatusCodeValue()).isEqualTo(401);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are incorrect in search by id */
    public void getStudentByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        TestRestTemplate template = restTemplate.withBasicAuth("123", "123");
        ResponseEntity<String> responsy = template.getForEntity("/v1/protected/students/8",
                String.class);
        assertThat(responsy.getStatusCodeValue()).isEqualTo(401);


    }


}
