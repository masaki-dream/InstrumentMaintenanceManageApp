package com.masaki.instrumentmaintenancemanageapp.controller;

import com.masaki.instrumentmaintenancemanageapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // コンストラクタ
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //ユーザー登録API
    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        userService.register(request.getUsername(), request.getPassword());

        return ResponseEntity.ok("ユーザー登録が完了しました。");
    }

    //リクエスト用DTOクラス(username と password を受け取る)
    public static class RegisterRequest {
        private String username;
        private String password;

        // ===== setter =====
        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        // ===== getter =====
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
