package com.example.social.media.api.service;

import com.example.social.media.api.dto.UserDto;
import com.example.social.media.api.dto.UserOwnerDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserDto getUserDtoByUsername(String username);

    List<UserOwnerDto> getPendingFriendRequests();
    ResponseEntity<String> sendFriendRequest(String username);
    ResponseEntity<String> acceptFriendRequest(String username);
    ResponseEntity<String> rejectFriendRequest(String username);
    ResponseEntity<String> cancelFriendRequest(String username);
    ResponseEntity<String> removeFriend(String username);
}
