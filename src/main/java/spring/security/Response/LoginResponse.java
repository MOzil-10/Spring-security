package spring.security.Response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }
}
