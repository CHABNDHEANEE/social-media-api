package com.example.social.media.api.controller;

import com.example.social.media.api.dto.UserDto;
import com.example.social.media.api.dto.UserOwnerDto;
import com.example.social.media.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private static final String FRIEND_USERNAME_HEADER = "X-friend-username";
    private UserService userService;

    @GetMapping("/{username}")
    public UserDto getUserByUsername(@PathVariable String username) {
        return userService.getUserDtoByUsername(username);
    }

    @GetMapping("/friends/request")
    public List<UserOwnerDto> getPendingFriendRequests() {
        return userService.getPendingFriendRequests();
    }

    @PostMapping("/friends/request/add")
    public ResponseEntity<String> addFriend(@RequestHeader(FRIEND_USERNAME_HEADER) String username) {
        return userService.sendFriendRequest(username);
    }

    @PostMapping("/friends/request/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestHeader(FRIEND_USERNAME_HEADER) String username) {
        return userService.acceptFriendRequest(username);
    }

    @PostMapping("/friends/request/reject")
    public ResponseEntity<String> rejectFriendRequest(@RequestHeader(FRIEND_USERNAME_HEADER) String username) {
        return userService.rejectFriendRequest(username);
    }

    @PostMapping("/friends/request/cancel")
    public ResponseEntity<String> cancelFriendRequest(@RequestHeader(FRIEND_USERNAME_HEADER) String username) {
        return userService.cancelFriendRequest(username);
    }
}
