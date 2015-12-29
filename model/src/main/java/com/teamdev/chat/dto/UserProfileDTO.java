package com.teamdev.chat.dto;

import java.util.Date;


public class UserProfileDTO {
    public final long id;
    public final String name;
    private final Date birthday;

    public UserProfileDTO(long id, String name, Date birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }
}