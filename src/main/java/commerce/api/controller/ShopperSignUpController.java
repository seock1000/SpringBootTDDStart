package commerce.api.controller;

import commerce.command.CreateShopperCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static commerce.UserPropertyValidator.isEmailValid;
import static commerce.UserPropertyValidator.isUsernameValid;

@RestController
public record ShopperSignUpController() {

    @PostMapping("/shopper/signUp")
    ResponseEntity<?> signUp(@RequestBody CreateShopperCommand command) {
        if (isCommandValid(command) == false) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateShopperCommand command) {
        return isEmailValid(command.email())
            && isUsernameValid(command.username());
    }
}
