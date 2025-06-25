package commerce.api.controller;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.CreateSellerCommand;
import lombok.RequiredArgsConstructor;
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
public record SellerSignUpController(
    PasswordEncoder passwordEncoder,
    SellerRepository sellerRepository
) {

    @PostMapping("/seller/signup")
    ResponseEntity<?> signUp(
        @RequestBody CreateSellerCommand command
    ) {
        if (!isCommandValid(command)) {
            return ResponseEntity.badRequest().build();
        }

        UUID id = UUID.randomUUID();
        String hashedPassword = passwordEncoder.encode(command.password());
        Seller seller = new Seller();
        seller.setId(id);
        seller.setEmail(command.email());
        seller.setUsername(command.username());
        seller.setHashedPassword(hashedPassword);
        sellerRepository.save(seller);

        return ResponseEntity.noContent().build();
    }

    private static boolean isCommandValid(CreateSellerCommand command) {
        return isEmailValid(command.email())
            && isUsernameValid(command.username())
            && isPasswordValid(command.password());
    }

}
