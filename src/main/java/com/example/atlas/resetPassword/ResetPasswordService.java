package com.example.atlas.resetPassword;

import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.util.SendEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class ResetPasswordService {

    private final ResetPasswordRepo resetPasswordRepo;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SendEmail sendEmail;

    public ResetPasswordService(ResetPasswordRepo resetPasswordRepo, UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder, SendEmail sendEmail) {
        this.resetPasswordRepo = resetPasswordRepo;
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sendEmail = sendEmail;
    }

    public void forgetPassword(String email) {
        Users users = userRepo.findUsersByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String token = UUID.randomUUID().toString();

        String content = "this is your token for reset your password: " + token;
        sendEmail.sendEmail(email, content);
        ResetPassword resetPassword = ResetPassword
                .builder()
                .users(users)
                .expireAt(LocalDateTime.now().plusMinutes(5))
                .token(token)
                .build();

        resetPasswordRepo.save(resetPassword);
    }

    public String resetPassword(String token, String newPassword) {

        ResetPassword resetPassword = resetPasswordRepo.findByToken(token)
                .orElseThrow(() -> new NotFoundException("invalid token"));

        if (resetPassword.isUsed() || resetPassword.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token is used or get Expired");
        }
        Users users = resetPassword.getUsers();


        users.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(users);
        resetPassword.setUsed(true);
        resetPasswordRepo.save(resetPassword);


        return "Password changed";
    }
}
