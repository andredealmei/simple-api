package br.com.andredealmei.endpoint;

import br.com.andredealmei.error.ResourceNotFoundException;
import br.com.andredealmei.model.Student;
import br.com.andredealmei.repository.StudentRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("v1")
public class StudentEndpoint {


    private final StudentRepository studentRepository;

    @Autowired
    public StudentEndpoint(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    /*
    * for pagination and sort list use this url below
    * /students?sort=name,asc&sort=email,desc&page=2
    */
    @GetMapping("protected/students")
    @ApiOperation(value = "Return a list with all students", response = Student[].class)
    public ResponseEntity<?> listAll(Pageable pageable) {

        return new ResponseEntity<>(studentRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/protected/students/{id}")
    @ApiOperation(value = "Return a student if id exists", response = Student.class)
    public ResponseEntity<?> getStudentByid(@PathVariable("id") Long id, Authentication userDetails) {


        verifyIfStudentExists(id);

        Optional<Student> student = studentRepository.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);

    }

    @PostMapping("admin/students")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "persist a student if all values is correct", response = Student.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        studentRepository.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);

    }

    @DeleteMapping("admin/students/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete a single student if id exists", response = HttpStatus.class)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudentExists(id);

        studentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);


    }

    @PutMapping("admin/students")
    @ApiOperation(value = "Update student if exists", response = Student.class)
    public ResponseEntity<?> update(@RequestBody Student student) {

        verifyIfStudentExists(student.getId());


        studentRepository.save(student);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("protected/students/name/{name}")
    @ApiOperation(value = "Return a list with students containing the name", response = Student[].class)
    public ResponseEntity<?> findStudentsByName(@PathVariable String name) {

        return new ResponseEntity<>(studentRepository.findByNameIgnoreCaseContaining(name), HttpStatus.OK);

    }


    private void verifyIfStudentExists(Long id){

        if (!studentRepository.findById(id).isPresent()) {

            throw new ResourceNotFoundException("Student not found for id : "+ id);

        }

    }


}
