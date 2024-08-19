package spring.security.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import spring.security.Model.User;
import spring.security.Response.LoginResponse;
import spring.security.Service.AuthService;
import spring.security.Service.JwtService;
import spring.security.dto.LoginDto;
import spring.security.dto.RegisterDto;

/**
 * REST controller for handling authentication-related requests such as registration and login.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;
    private final JwtService jwtService;

    /**
     * Registers a new user.
     *
     * @param registerUserDto The data transfer object containing user registration details.
     * @return The registered User entity wrapped in a ResponseEntity.
     */
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterDto registerUserDto) {
        User registeredUser = service.registerUser(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param loginUserDto The data transfer object containing user login details.
     * @return A LoginResponse containing the JWT token and expiration time, wrapped in a ResponseEntity.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDto loginUserDto) {
        User authenticatedUser = service.authenicateUser(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Handles IllegalArgumentException thrown during authentication or registration.
     *
     * @param ex      The exception thrown.
     * @param request The web request during which the exception occurred.
     * @return A ResponseEntity containing the error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
