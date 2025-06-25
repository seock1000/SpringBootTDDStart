package commerce.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.api.JwtKeyHolder;
import commerce.query.IssueSellerToken;
import commerce.result.AccessTokenCarrier;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.SecretKeySpec;

@RestController
public record SellerIssueTokenController(
    PasswordEncoder passwordEncoder,
    SellerRepository sellerRepository,
    JwtKeyHolder jwtKeyHolder
) {

    @PostMapping("/seller/issueToken")
    ResponseEntity<AccessTokenCarrier> issueToken(@RequestBody IssueSellerToken query) {
        return sellerRepository.findByEmail(query.email())
            .filter(seller -> passwordEncoder.matches(query.password(), seller.getHashedPassword()))
            .map(seller -> composeToken())
            .map(AccessTokenCarrier::new)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private String composeToken() {
        return Jwts
            .builder()
            .setSubject("seller")
            .signWith(jwtKeyHolder.key())
            .compact();
    }
}
