package commerce.api.controller;

import javax.crypto.spec.SecretKeySpec;

import commerce.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerIssueTokenController(
    @Value("${security.jwt.secret}") String jwtSecret
) {

    @PostMapping("/seller/issueToken")
    AccessTokenCarrier issueToken() {
        return new AccessTokenCarrier(
            Jwts
                .builder()
                .signWith(new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256"))
                .compact()
        );
    }
}
