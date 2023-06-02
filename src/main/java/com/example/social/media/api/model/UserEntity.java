package com.example.social.media.api.model;

import com.example.social.media.api.auxilary.DtoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_role_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "followers", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"))
    private List<Friend> followers = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "followers", joinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<Friend> followings = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "friend_requests", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "follower_id", referencedColumnName = "id"))
    private List<Friend> pendingRequests = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "friends", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    private List<Friend> friends = new ArrayList<>();

    public void deleteFollowing(UserEntity following) {
        followings.remove(DtoMapper.userEntityToFriend(following));
    }

    public void sendRequest(UserEntity sender) {
        Friend senderFriend = DtoMapper.userEntityToFriend(sender);

        followers.add(senderFriend);
        pendingRequests.add(senderFriend);
    }

    public void acceptRequest(UserEntity sender) {
        Friend senderFriend = DtoMapper.userEntityToFriend(sender);

        addFriend(sender);
        sender.addFriend(this);
        followings.add(senderFriend);
        pendingRequests.remove(senderFriend);
    }

    public void rejectRequest(UserEntity sender) {
        pendingRequests.remove(DtoMapper.userEntityToFriend(sender));
    }

    public void cancelRequest(UserEntity sender) {
        Friend senderFriend = DtoMapper.userEntityToFriend(sender);

        followers.remove(senderFriend);
        pendingRequests.remove(senderFriend);
    }

    public void removeFriend(UserEntity sender) {
        this.removeFriendFromList(sender);
        sender.removeFriendFromList(this);

        sender.removeFollower(this);
        deleteFollowing(sender);
    }

    private void addFriend(UserEntity friend) {
        friends.add(DtoMapper.userEntityToFriend(friend));
    }

    private void removeFriendFromList(UserEntity friend) {
        friends.remove(DtoMapper.userEntityToFriend(friend));
    }

    private void removeFollower(UserEntity follower) {
        followers.remove(DtoMapper.userEntityToFriend(follower));
    }
}
