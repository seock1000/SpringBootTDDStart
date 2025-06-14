package commerce.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerSignUpController() {

    @PostMapping("/seller/signup")
    ResponseEntity<?> signUp() {
        return ResponseEntity.noContent().build();
    }
}
