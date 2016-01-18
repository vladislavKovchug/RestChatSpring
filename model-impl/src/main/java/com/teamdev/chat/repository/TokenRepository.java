package com.teamdev.chat.repository;

import com.teamdev.chat.entity.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long>{


    @Query("SELECT e FROM #{#entityName} e WHERE e.token = :token")
    Token findByToken(@Param("token") String token);


}
