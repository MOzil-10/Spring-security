package spring.security.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.security.Model.User;
import spring.security.Repository.UserRepository;
import spring.security.dto.LoginDto;
import spring.security.dto.RegisterDto;
import spring.security.exception.UserServiceException;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user with the given details.
     *
     * @param userInput the registration details provided by the user
     * @return the registered User entity
     * @throws UserServiceException if a user with the same email already exists
     */
    public User registerUser(RegisterDto userInput) {
        Optional<User> existingUser = userRepository.findByEmail(userInput.getEmail());
        if (existingUser.isPresent()) {
            throw new UserServiceException("User with email " + userInput.getEmail() + " already exists", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setFullName(userInput.getFullName());
        user.setEmail(userInput.getEmail());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param userInput the login details provided by the user
     * @return the authenticated User entity
     * @throws UserServiceException if the credentials are invalid or the user is not found
     */
    public User authenticateUser(LoginDto userInput) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userInput.getEmail(),
                            userInput.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new UserServiceException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return userRepository.findByEmail(userInput.getEmail())
                .orElseThrow(() -> new UserServiceException("User not found", HttpStatus.NOT_FOUND));
    }
}
