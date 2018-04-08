package br.com.andredealmei;

import br.com.andredealmei.model.Student;
import br.com.andredealmei.repository.StudentRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository studentRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test/* test persist  */
    public void createShouldPersistData() {

        Student student = new Student("Andre", "email@andre.com");
        this.studentRepository.save(student);
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo("Andre");
        assertThat(student.getEmail()).isEqualTo("email@andre.com");


    }

    @Test /* test delet and update */
    public void updateShouldChangeAndPersistData() {
        Optional<Student> student = Optional.of(new Student("Joana", "email@joana.com"));
        this.studentRepository.save(student.get());
        student.get().setName("Lucas");
        student.get().setEmail("email@lucas.com");
        this.studentRepository.save(student.get());
        assertThat(student.get().getName()).isEqualTo("Lucas");
        assertThat(student.get().getEmail()).isEqualTo("email@lucas.com");

        student = studentRepository.findById(student.get().getId());
        this.studentRepository.delete(student.get());
        assertThat(studentRepository.findById(student.get().getId())).isEmpty();
    }

    @Test /* test find by name ignoring case */
    public void findByNameIgnoreCaseShouldIgnoreCase() {
        Student student = new Student("Joana", "email@joana.com");
        Student student2 = new Student("joana", "email@joana.com");
        this.studentRepository.save(student);
        this.studentRepository.save(student2);

        List<Student> list = studentRepository.findByNameIgnoreCaseContaining("joana");

        assertThat(list.size()).isEqualTo(2);

    }




}
