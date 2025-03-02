package commerce.api.controller;

import java.security.Principal;
import java.util.UUID;

import commerce.view.SellerMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerMeController() {

    @GetMapping("/seller/me")
    SellerMeView me(Principal user) {
        UUID id = UUID.fromString(user.getName());
        return new SellerMeView(id, null, null);
    }
}
