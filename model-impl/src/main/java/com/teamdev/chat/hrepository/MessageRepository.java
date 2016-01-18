package com.teamdev.chat.hrepository;

import com.teamdev.chat.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("SELECT e FROM #{#entityName} e WHERE (e.userTo = :userId or e.userTo is null) and " +
            "e.chatRoom = :chatRoom and e.date > :date")
    List<Message> findAllUserMessagesAfter(@Param("userId") Long userId, @Param("chatRoom") Long chatRoom, @Param("date") Date date);

}
