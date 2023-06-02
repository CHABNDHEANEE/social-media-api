package com.example.social.media.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {

    private String username;
    private List<UserInListDto> friends;
    private List<UserInListDto> followers;
    private List<UserInListDto> followings;
}
