package commerce.api.controller;

import commerce.Shopper;
import commerce.ShopperRepository;
import commerce.command.CreateShopperCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static commerce.UserPropertyValidator.isEmailValid;
import static commerce.UserPropertyValidator.isPasswordValid;
import static commerce.UserPropertyValidator.isUsernameValid;

@RestController
public record ShopperSignUpController(
    ShopperRepository shopperRepository,
    PasswordEncoder encoder
) {

    @PostMapping("/shopper/signup")
    ResponseEntity<?> signUp(
        @RequestBody CreateShopperCommand command
    ) {
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        UUID id = UUID.randomUUID();
        String hashedPassword = encoder.encode(command.password());
        var shopper = new Shopper();
        shopper.setId(id);
        shopper.setEmail(command.email());
        shopper.setUsername(command.username());
        shopper.setHashedPassword(hashedPassword);
        shopperRepository.save(shopper);
        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateShopperCommand command) {
        return isEmailValid(command.email())
            && isUsernameValid(command.username())
            && isPasswordValid(command.password());
    }
}
