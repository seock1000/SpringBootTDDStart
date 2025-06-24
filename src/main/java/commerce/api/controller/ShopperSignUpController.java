package commerce.api.controller;

import commerce.command.CreateShopperCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperSignUpController() {

    @PostMapping("/shopper/signup")
    ResponseEntity<?> signUp(
        @RequestBody CreateShopperCommand command
    ) {
        if (command.email() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }

}
