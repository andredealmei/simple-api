package br.com.andredealmei.JavaClient;

import br.com.andredealmei.model.Student;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class JavaClienteSpring {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("http://localhost:8080/v1/protected/students")
                .basicAuthorization("andre","123").build();

        Student forObject = restTemplate.getForObject("/{id}", Student.class, 8);
        ResponseEntity<Student> forEntity = restTemplate.getForEntity("/{id}", Student.class, 8);
        System.out.println(forObject);
        System.out.println(forEntity.getBody());

/*        Student[] students = restTemplate.getForObject("/", Student[].class, 8);
        System.out.println(Arrays.toString(students));

        ResponseEntity<List<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });
        System.out.println("**********for each**********");
        exchange.getBody().forEach(System.out::println);
*/
        ResponseEntity<List<Student>> exchange = restTemplate.exchange("/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Student>>() {
                });




    }
}
