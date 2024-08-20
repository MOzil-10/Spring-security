package spring.security.Response;

import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Data
public class LoginResponse {
    private String token;
    private long expiresInMinutes;
    private String issuedAt;
    private Map<String, Object> claims;

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresInMinutes(long expiresIn) {
        this.expiresInMinutes = expiresIn;
        return this;
    }

    public LoginResponse setIssuedAt(long issuedAt) {
        this.issuedAt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(Instant.ofEpochMilli(issuedAt));
        return this;
    }

    public LoginResponse setClaims(Map<String, Object> claims) {
        this.claims = claims;
        return this;
    }
}
