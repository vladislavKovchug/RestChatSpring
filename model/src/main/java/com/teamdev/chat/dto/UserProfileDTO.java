package com.teamdev.chat.dto;

import java.util.Date;


public class UserProfileDTO {
    public final Long id;
    public final String name;
    public final Long age;
    private final Date birthday;

    public UserProfileDTO(Long id, String name, Long age, Date birthday) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }
}