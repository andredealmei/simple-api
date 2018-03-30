package br.com.andredealmei.repository;

import br.com.andredealmei.model.Student;
import org.springframework.data.repository.PagingAndSortingRepository;


import java.util.List;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {

    List<Student> findByNameIgnoreCaseContaining(String name);


}
