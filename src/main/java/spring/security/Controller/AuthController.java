package spring.security.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.security.Model.User;
import spring.security.Response.LoginResponse;
import spring.security.Service.AuthService;
import spring.security.Service.JwtService;
import spring.security.dto.LoginDto;
import spring.security.dto.RegisterDto;

import java.util.Date;
import java.util.Map;

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
     * @return A ResponseEntity containing the registered User entity.
     */
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterDto registerUserDto) {
        User registeredUser = service.registerUser(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Authenticates a user and returns a JWT token along with its details.
     *
     * @param loginUserDto The data transfer object containing user login details.
     * @return A ResponseEntity containing a LoginResponse with the JWT token, expiration time, issued at time, and claims.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDto loginUserDto) {
        User authenticatedUser = service.authenicateUser(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        Date issuedAt = jwtService.extractIssuedAt(jwtToken);
        Long expiresInMinutes = jwtService.getExpirationTime() / 1000 / 60;
        Map<String, Object> claims = jwtService.extractAllClaims(jwtToken);

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresInMinutes(expiresInMinutes)
                .setIssuedAt(issuedAt.getTime())
                .setClaims(claims);

        return ResponseEntity.ok(loginResponse);
    }

    /**
     * Handles IllegalArgumentException thrown during authentication or registration.
     *
     * @param ex The exception thrown.
     * @return A ResponseEntity containing the error message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
