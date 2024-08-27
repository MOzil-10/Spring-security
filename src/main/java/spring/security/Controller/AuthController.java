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
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;
    private final JwtService jwtService;

    public AuthController(AuthService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user.
     *
     * @param registerUserDto the data transfer object containing user registration details
     * @return a ResponseEntity containing the registered User entity
     */
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterDto registerUserDto) {
        User registeredUser = service.registerUser(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Authenticates a user and returns a JWT token along with its details.
     *
     * @param loginUserDto the data transfer object containing user login details
     * @return a ResponseEntity containing a LoginResponse with the JWT token, expiration time, issued at time, and claims
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDto loginUserDto) {
        User authenticatedUser = service.authenticateUser(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        Date issuedAt = jwtService.extractIssuedAt(jwtToken);
        long expiresInMinutes = jwtService.getExpirationTime() / 1000 / 60;
        Map<String, Object> claims = jwtService.extractAllClaims(jwtToken);

        LoginResponse loginResponse = new LoginResponse()
                .setToken(jwtToken)
                .setExpiresInMinutes(expiresInMinutes)
                .setIssuedAt(issuedAt.getTime())
                .setClaims(claims);

        return ResponseEntity.ok(loginResponse);
    }

}
