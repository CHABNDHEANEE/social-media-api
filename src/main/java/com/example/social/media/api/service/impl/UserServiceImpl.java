package com.example.social.media.api.service.impl;

import com.example.social.media.api.auxilary.DtoMapper;
import com.example.social.media.api.dto.UserDto;
import com.example.social.media.api.dto.UserOwnerDto;
import com.example.social.media.api.exception.FriendRequestException;
import com.example.social.media.api.model.UserEntity;
import com.example.social.media.api.repository.UserRepository;
import com.example.social.media.api.security.IAuthenticationFacade;
import com.example.social.media.api.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private IAuthenticationFacade authenticationFacade;
    private UserRepository userRepository;

    @Override
    public UserDto getUserDtoByUsername(String username) {
        return DtoMapper.userEntityToUserDto(getUserByUsername(username));
    }

    @Override
    public List<UserOwnerDto> getPendingFriendRequests() {
        UserEntity user = getCurrentUser();

        return user.getPendingRequests().stream()
                .map(o -> DtoMapper.userEntityToOwnerDto(getUserByUsername(o.getUsername())))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> sendFriendRequest(String friendUsername) {
        UserEntity user = getCurrentUser();
        UserEntity friend = getUserByUsername(friendUsername);

        if (user.getFriends().contains(DtoMapper.userEntityToFriend(friend))) {
            throw new FriendRequestException(String.format("%s already your friend", friendUsername));
        } else if (user.getFollowings().contains(DtoMapper.userEntityToFriend(friend))) {
            throw new FriendRequestException("You already following " + friendUsername);
        }

        if (checkFollower(user, friend)) {
            acceptFriendRequest(friendUsername);
        }

        friend.sendRequest(user);

        userRepository.save(user);
        userRepository.save(friend);

        log.info("{} sent friend request to {}", user.getUsername(), friend.getUsername());

        return new ResponseEntity<>("Friend request successfully!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> acceptFriendRequest(String friendUsername) {
        UserEntity user = getCurrentUser();
        UserEntity friend = getUserByUsername(friendUsername);

        if (!checkFollower(user, friend)) {
            throw new FriendRequestException(String.format("%s doesn't send you a friend request!", friendUsername));
        }

        user.acceptRequest(friend);
        friend.addFriend(user);

        userRepository.save(user);
        userRepository.save(friend);

        log.info("{} accepted friend request from {}", user.getUsername(), friend.getUsername());

        return new ResponseEntity<>("Friend request is accepted!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> rejectFriendRequest(String friendUsername) {
        UserEntity user = getCurrentUser();
        UserEntity friend = getUserByUsername(friendUsername);

        if (!checkFollower(user, friend)) {
            throw new FriendRequestException(String.format("%s doesn't send you a friend request!", friendUsername));
        }

        user.rejectRequest(friend);

        log.info("{} reject friend request from {}", user.getUsername(), friend.getFriends());

        return new ResponseEntity<>("Friend request rejected!", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> cancelFriendRequest(String friendUsername) {
        UserEntity user = getCurrentUser();
        UserEntity friend = getUserByUsername(friendUsername);

        if (!checkFollower(friend, user)) {
            throw new FriendRequestException("You doesn't send a friend request to " + friendUsername);
        }

        user.deleteFollowing(friend);
        friend.rejectRequest(user);

        log.info("{} cancel friend request to {}", user.getUsername(), friend.getUsername());

        return new ResponseEntity<>("Your friend request is canceled!", HttpStatus.OK);
    }

    private UserEntity getCurrentUser() {
        //noinspection OptionalGetWithoutIsPresent
        return userRepository.findByUsername(authenticationFacade.getAuthentication().getName()).get();
    }

    private UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found!", username)));

    }

    private boolean checkFollower(UserEntity user, UserEntity follower) {
        return user.getFollowers().contains(DtoMapper.userEntityToFriend(follower));
    }
}
