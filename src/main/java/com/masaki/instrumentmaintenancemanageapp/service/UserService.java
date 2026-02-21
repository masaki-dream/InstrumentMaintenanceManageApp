package com.masaki.instrumentmaintenancemanageapp.service;

import com.masaki.instrumentmaintenancemanageapp.exception.BadRequestException;
import com.masaki.instrumentmaintenancemanageapp.infrastructure.UserEntity;
import com.masaki.instrumentmaintenancemanageapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // コンストラクタ
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ユーザー登録処理
    public void register(String username, String rawPassword) {

        // 既に同じusernameが存在するかチェック(400)
        if (userRepository.findByUsername(username).isPresent()) {
            throw new BadRequestException("このユーザー名は既に使用されています。");
        }

        // パスワードを暗号化
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Entity生成
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        // DB保存
        userRepository.save(user);
    }
}
