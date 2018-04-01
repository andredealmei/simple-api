package br.com.andredealmei.endpoint;

import br.com.andredealmei.error.ResourceNotFoundException;
import br.com.andredealmei.model.Student;
import br.com.andredealmei.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;


@RestController
@RequestMapping("students")
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
    @GetMapping()
    public ResponseEntity<?> listAll(Pageable pageable) {

        return new ResponseEntity<>(studentRepository.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentByid(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println(userDetails);

        verifyIfStudentExists(id);

        Optional<Student> student = studentRepository.findById(id);
        return new ResponseEntity<>(student, HttpStatus.OK);

    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        studentRepository.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        verifyIfStudentExists(id);

        studentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);


    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Student student) {

        verifyIfStudentExists(student.getId());


        studentRepository.save(student);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable String name) {

        return new ResponseEntity<>(studentRepository.findByNameIgnoreCaseContaining(name), HttpStatus.OK);

    }

    private void verifyIfStudentExists(Long id){

        if (!studentRepository.findById(id).isPresent()) {

            throw new ResourceNotFoundException("Student not found for id : "+ id);

        }

    }


}
