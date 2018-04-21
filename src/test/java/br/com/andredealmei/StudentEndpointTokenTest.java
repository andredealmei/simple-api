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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentEndpointTokenTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    private HttpEntity<Void> protectedHeader;
    private HttpEntity<Void> adminHeader;
    private HttpEntity<Void> wrongHeader;

    @Before
    public void configProtectedHeaders() {

        String str = "{\"userName\":\"usuario\",\"password\":\"123\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.protectedHeader = new HttpEntity<>(headers);
    }


    @Before
    public void configAdminHeaders() {

        String str = "{\"userName\":\"andre\",\"password\":\"123\"}";
        HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
        this.adminHeader = new HttpEntity<>(headers);
    }


    @Before
    public void configWrongHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "111111111111111");
        this.wrongHeader = new HttpEntity<>(headers);
    }


    @Before
    public void setup() {
        Student student = new Student(1L, "Andre", "andre@email.com");
        BDDMockito.when(studentRepository.findById(student.getId())).thenReturn(java.util.Optional.ofNullable(student));
    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are incorrect */
    public void listStudentsWhenTokenIsIncorrectShouldReturnStatusCode403() {

        ResponseEntity<String> responsy = restTemplate.exchange("/v1/protected/students/", GET, wrongHeader,
                String.class);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(403);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct */
    public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {

        ResponseEntity<String> responsy = restTemplate.exchange("/v1/protected/students/", GET, protectedHeader,
                String.class);
        assertThat(responsy.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are incorrect in search by id */
    public void getStudentByIdWhenTokenIsIncorrectShouldReturnStatusCode401() {

        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/1", GET, wrongHeader,
                String.class);

        assertThat(response.getStatusCodeValue()).isEqualTo(403);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct in search by existing id */
    public void getStudentByIdWhenTokenIsCorrectShouldReturnStatusCode200() {

        ResponseEntity<String> responsy = restTemplate.exchange("/v1/protected/students/{id}", GET, protectedHeader,
                String.class, 1L);

        assertThat(responsy.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password are correct in search by non-existing id */
    public void getStudentByIdWhenTokenAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {

        ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/{id}", GET, protectedHeader,
                String.class, -1);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);


    }

    @Test /* test if  returned correct HttpStatusCode  when username or password of a ADMIN are correct and try to delete a  existing id */
    public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);


        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}",
                DELETE, adminHeader, String.class, 1L);

        assertThat(exchange.getStatusCodeValue()).isEqualTo(200);


    }

    @Test /* test if  returned correct HttpStatusCode  when a ADMIN are try to delete a non-existing id */
    public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistsShouldReturnStatusCode404() throws Exception {

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        String token = adminHeader.getHeaders().get("Authorization").get(0);


        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", -1L).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
                );


    }

    @Test /* test if  returned correct HttpStatusCode  when a user with role "USER" try to delete something  */
    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {

        BDDMockito.doNothing().when(studentRepository).deleteById(1L);

        String token = protectedHeader.getHeaders().get("Authorization").get(0);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v1/admin/students/{id}", 1L).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isForbidden()
                );


    }

    @Test /* test if  returned correct HttpStatusCode  when a ADMIN try create a student without a name */
    public void createWhenNameIsNullShouldReturnsCode400() throws Exception {

        Student student = new Student(3L, null, "joao@email.com");

        BDDMockito.when(studentRepository.save(student)).thenReturn(student);

        ResponseEntity<String> entity = restTemplate.exchange("/v1/admin/students/", POST, new HttpEntity<>(student, adminHeader.getHeaders()), String.class);

        assertThat(entity.getStatusCodeValue()).isEqualTo(400);
        assertThat(entity.getBody()).contains("fieldMessage", "o campo nome é obrigatorio");

    }

    @Test /* test if  returned correct HttpStatusCode  when a ADMIN create a student with success */
    public void createShouldReturnsStatusCode201() throws Exception {

        Student student = new Student(3L, "andre", "joao@email.com");

        BDDMockito.when(studentRepository.save(student)).thenReturn(student);

        ResponseEntity<Student> entity = restTemplate.exchange("/v1/admin/students/", POST, new HttpEntity<>(student, adminHeader.getHeaders()), Student.class);

        assertThat(entity.getBody().getId()).isNotNull();
        assertThat(entity.getBody().getName()).isEqualTo("andre");
        assertThat(entity.getBody().getEmail()).isEqualTo("joao@email.com");
        assertThat(entity.getStatusCodeValue()).isEqualTo(201);

    }

}
