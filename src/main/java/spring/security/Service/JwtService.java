package spring.security.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private Long jwtExpiration;

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token The JWT token.
     * @param claimsResolver Function to extract the claim from Claims.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token based on user details.
     *
     * @param userDetails The user details for whom the token is to be generated.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    /**
     * Generates a JWT token with custom claims and user details.
     *
     * @param extractClaims Custom claims to include in the token.
     * @param userDetails The user details for whom the token is to be generated.
     * @return The generated JWT token.
     */
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
        return buildToken(extractClaims, userDetails, jwtExpiration);
    }

    /**
     * Retrieves the expiration time for the JWT token.
     *
     * @return The expiration time in milliseconds.
     */
    public Long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Builds the JWT token with the specified claims, user details, and expiration time.
     *
     * @param extractClaims Custom claims to include in the token.
     * @param userDetails The user details for whom the token is to be generated.
     * @param expiration The expiration time in milliseconds.
     * @return The generated JWT token.
     */
    private String buildToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails,
            long expiration
    ) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT token against the provided user details.
     *
     * @param token The JWT token.
     * @param userDetails The user details for validation.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token The JWT token.
     * @return True if the token is expired, false otherwise.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token.
     * @return The expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the issued at date from the JWT token.
     *
     * @param token The JWT token.
     * @return The issued at date.
     */
    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token.
     * @return The claims contained in the token.
     */
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key used for token signing.
     *
     * @return The signing key.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}
