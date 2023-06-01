package com.example.social.media.api.controller;

import com.example.social.media.api.security.IAuthenticationFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/home")
@Slf4j
public class UserController {

    private IAuthenticationFacade authenticationFacade;

    @GetMapping("/user")
    @ResponseBody
    public String currentUserName() {
        log.info("here");
        log.info(authenticationFacade.getAuthentication().getName());
        return authenticationFacade.getAuthentication().getName();
    }
}
