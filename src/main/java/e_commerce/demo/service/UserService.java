package e_commerce.demo.service;

import e_commerce.demo.dto.ChangePasswordRequest;
import e_commerce.demo.exception.ResourceNotFoundException;
import e_commerce.demo.model.User;
import e_commerce.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        user.setEmailConfirmationCode(generateConfirmationCode());
        user.setEmailConfirmation(false);
        emailService.sendConfirmationCode(user);
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with this email not found"));
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void confirmEmail(String email, String confirmationCode) {
        User user = getUserByEmail(email);
        if (confirmationCode.equals(user.getEmailConfirmationCode())) {
            user.setEmailConfirmationCode(null);
            user.setEmailConfirmation(true);
            userRepository.save(user);
        } else {
            throw new BadCredentialsException("Confirmation code is incorrect");
        }

    }

    private String generateConfirmationCode() {
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

}
