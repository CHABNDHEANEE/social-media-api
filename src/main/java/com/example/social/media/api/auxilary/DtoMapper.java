package com.example.social.media.api.auxilary;

import com.example.social.media.api.dto.UserDto;
import com.example.social.media.api.dto.UserInListDto;
import com.example.social.media.api.dto.UserOwnerDto;
import com.example.social.media.api.model.Friend;
import com.example.social.media.api.model.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class DtoMapper {

    public static UserDto userEntityToUserDto(UserEntity user) {
        return UserDto.builder()
                .username(user.getUsername())
                .friends(mapUserForInList(user.getFriends()))
                .followers(mapUserForInList(user.getFollowers()))
                .followings(mapUserForInList(user.getFollowings()))
                .build();
    }

    public static UserOwnerDto userEntityToOwnerDto(UserEntity user) {
        return UserOwnerDto.builder()
                .friends(mapUserForInList(user.getFriends()))
                .followers(mapUserForInList(user.getFollowers()))
                .followings(mapUserForInList(user.getFollowings()))
                .pendingRequests(mapUserForInList(user.getPendingRequests()))
                .build();
    }

    public static Friend userEntityToFriend(UserEntity user) {
        return Friend.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }

    private static List<UserInListDto> mapUserForInList(List<Friend> list) {
        return list.stream()
                .map(DtoMapper::convertToUserInListDto)
                .collect(Collectors.toList());
    }

    private static UserInListDto convertToUserInListDto(Friend user) {
        return UserInListDto.builder()
                .username(user.getUsername())
                .build();
    }
}
