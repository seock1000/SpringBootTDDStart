package commerce.api.controller;

import commerce.api.JwtKeyHolder;
import commerce.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperIssueTokenController(JwtKeyHolder jwtKeyHolder) {

    @PostMapping("/shopper/issueToken")
    public AccessTokenCarrier issueToken() {
        return new AccessTokenCarrier(
            Jwts
                .builder()
                .signWith(jwtKeyHolder.key())
                .compact()
        );
    }

}
