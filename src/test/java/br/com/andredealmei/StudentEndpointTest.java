package br.com.andredealmei;

import br.com.andredealmei.model.Student;
import br.com.andredealmei.repository.StudentRepository;
import org.junit.Before;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Before
    public void setup() {
        Student student = new Student(1L, "Andre", "andre@email.com");
        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.of(student));
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

        ResponseEntity<String> responsy = restTemplate.getForEntity("/v1/protected/students/{id}",
                String.class, 1L);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct in search by non-existing id */
    public void getStudentByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {

        ResponseEntity<String> responsy = restTemplate.getForEntity("/v1/protected/students/{id}",
                String.class, -1);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(404);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password of a ADMIN are correct and try to delete a  existing id */
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        TestRestTemplate template = restTemplate.withBasicAuth("andre", "123");

        ResponseEntity<String> exchange = template.exchange("/v1/admin/students/{id}",
                HttpMethod.DELETE, null, String.class, 1L);

        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when a ADMIN are try to delete a non-existing id */
    @WithMockUser(username = "andre",password = "123",roles = "ADMIN")
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception {

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        /*TestRestTemplate template = restTemplate.withBasicAuth("andre", "123");

        ResponseEntity<String> exchange = template.exchange("/v1/admin/students/{id}",
                HttpMethod.DELETE, null, String.class, -1L);

        assertThat(exchange.getStatusCodeValue()).isEqualTo(404);*/

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}",-1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
        );


    }

    @Test /* test if  returned correct HttpStatusCode  when a user with role "USER" try to delete something  */
    @WithMockUser(username = "andre",password = "123",roles = "USER")
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}",1L))
                .andExpect(MockMvcResultMatchers.status().isForbidden()
        );


    }

    @Test /* test if  returned correct HttpStatusCode  when a ADMIN try create a student without a name */
    public void createWhenNameIsNullShouldReturnsCode400() {

        Student student = new Student(3L, null, "joao@email.com");

        BDDMockito.when(studentRepository.save(student)).thenReturn(student);

        TestRestTemplate template = restTemplate.withBasicAuth("andre", "123");
        ResponseEntity<String> entity = template.postForEntity("/v1/admin/students/", student, String.class);

        assertThat(entity.getStatusCodeValue()).isEqualTo(400);
        assertThat(entity.getBody()).contains("fieldMessage", "o campo nome é obrigatorio");

    }

    @Test /* test if  returned correct HttpStatusCode  when a ADMIN create a student with success */
    public void createShouldReturnsStatusCode201() {

        Student student = new Student(3L, "andre", "joao@email.com");

        BDDMockito.when(studentRepository.save(student)).thenReturn(student);

        TestRestTemplate template = restTemplate.withBasicAuth("andre", "123");
        ResponseEntity<Student> entity = template.postForEntity("/v1/admin/students/", student, Student.class);

        assertThat(entity.getBody().getId()).isNotNull();
        assertThat(entity.getBody().getName()).isEqualTo("andre");
        assertThat(entity.getBody().getEmail()).isEqualTo("joao@email.com");
        assertThat(entity.getStatusCodeValue()).isEqualTo(201);

    }

}
