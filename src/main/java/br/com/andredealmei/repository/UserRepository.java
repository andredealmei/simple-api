package br.com.andredealmei.repository;

import br.com.andredealmei.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User,Long> {

    User findByUserName(String userName);

}
