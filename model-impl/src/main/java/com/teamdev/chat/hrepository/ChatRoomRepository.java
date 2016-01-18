package com.teamdev.chat.hrepository;


import com.teamdev.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.name = :name")
    ChatRoom findChatRoomByName(@Param("name") String name);

}
