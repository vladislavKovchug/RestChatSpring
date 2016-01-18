package com.teamdev.chat.hrepository;

import com.teamdev.chat.entity.Cat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends CrudRepository<Cat, Long> {

}
