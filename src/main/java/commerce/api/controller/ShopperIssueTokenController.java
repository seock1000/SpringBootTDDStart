package commerce.api.controller;

import commerce.ShopperRepository;
import commerce.api.JwtKeyHolder;
import commerce.query.IssueSellerToken;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperIssueTokenController(
    JwtKeyHolder jwtKeyHolder,
    ShopperRepository shopperRepository,
    PasswordEncoder passwordEncoder
) {

    @PostMapping("/shopper/issueToken")
    public ResponseEntity<AccessTokenCarrier> issueToken(
        @RequestBody IssueShopperToken query
        ) {
        return shopperRepository
            .findByEmail(query.email())
            .filter(shopper ->
                passwordEncoder.matches(query.password(), shopper.getHashedPassword())
            )
            .map(shopper -> composeToken())
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken() {
        return Jwts
                .builder()
                .signWith(jwtKeyHolder.key())
                .compact();
    }
}
