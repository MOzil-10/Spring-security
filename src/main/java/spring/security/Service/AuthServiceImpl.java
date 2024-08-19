package spring.security.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.Model.User;
import spring.security.Repository.UserRepository;
import spring.security.dto.LoginDto;
import spring.security.dto.RegisterDto;

import java.util.Optional;

/**
 * Implementation of the AuthService interface that handles user authentication and registration.
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user in the system.
     *
     * @param userInput The data transfer object containing user registration details.
     * @return The registered User entity.
     * @throws IllegalArgumentException if a user with the provided email already exists.
     */
    public User registerUser(RegisterDto userInput) {
        Optional<User> existingUser = userRepository.findByEmail(userInput.getEmail());
        if(existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email " + userInput.getEmail() + "already exist");
        }

        User user = new User();
        user.setFullName(userInput.getFullName());
        user.setEmail(userInput.getEmail());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Authenticates a user based on the provided login credentials.
     *
     * @param userInput The data transfer object containing user login details.
     * @return The authenticated User entity.
     * @throws IllegalArgumentException if the credentials are invalid or the user is not found.
     */
    public User authenicateUser(LoginDto userInput) {

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInput.getEmail(),
                            userInput.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return userRepository.findByEmail(userInput.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
    }
}
