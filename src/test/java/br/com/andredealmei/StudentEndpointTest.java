package br.com.andredealmei;

import br.com.andredealmei.model.Student;
import br.com.andredealmei.repository.StudentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
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

import java.util.Arrays;
import java.util.List;

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

            return new RestTemplateBuilder().basicAuthorization("usuario", "123");

        }

    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are incorrect */
    public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        TestRestTemplate template = restTemplate.withBasicAuth("123", "123");

        ResponseEntity<String> responsy = template.getForEntity("/v1/protected/students/",
                String.class);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(401);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct */
    public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
        List<Student> students = Arrays.asList(
                new Student(1L, "Andre", "andre@email.com"),
                new Student(2L, "Pedro", "pedro@email.com"),
                new Student(3L, "Alan", "alan@email.com")
        );

        BDDMockito.when(studentRepository.findAll()).thenReturn(students);

        ResponseEntity<String> responsy = restTemplate.getForEntity("/v1/protected/students/",
                String.class);
        assertThat(responsy.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are incorrect in search by id */
    public void getStudentByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
        TestRestTemplate template = restTemplate.withBasicAuth("123", "123");

        ResponseEntity<String> responsy = template.getForEntity("/v1/protected/students/8",
                String.class);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(401);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct in search by existing id */
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {

        Student student = new Student(1L, "Andre", "andre@email.com");

        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.ofNullable(student));

        ResponseEntity<String> responsy = restTemplate.getForEntity("/v1/protected/students/{id}",
                String.class, student.getId());

        assertThat(responsy.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct in search by non-existing id */
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {

        Student student = new Student(1L, "Andre", "andre@email.com");

        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.ofNullable(student));

        ResponseEntity<String> responsy = restTemplate.getForEntity("/v1/protected/students/{id}",
                String.class, 50L);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(404);


    }


}
