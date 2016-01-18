package com.teamdev.chat.hrepository;


import com.teamdev.chat.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.login = :login")
    User findUserByLogin(@Param("login") String login);

}
