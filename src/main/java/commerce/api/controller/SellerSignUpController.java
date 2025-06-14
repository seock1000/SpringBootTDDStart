package commerce.api.controller;

import commerce.command.CreateSellerCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerSignUpController() {

    @PostMapping("/seller/signup")
    ResponseEntity<?> signUp(
        @RequestBody CreateSellerCommand command
    ) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if(command.email() == null) {
            return ResponseEntity.badRequest().build();
        } else if(!command.email().contains("@")) {
            return ResponseEntity.badRequest().build();
        } else if(command.email().endsWith("@")) {
            return ResponseEntity.badRequest().build();
        } else if(!command.email().matches(emailRegex)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.noContent().build();
    }
}
