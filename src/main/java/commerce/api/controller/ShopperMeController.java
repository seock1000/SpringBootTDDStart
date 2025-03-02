package commerce.api.controller;

import java.security.Principal;
import java.util.UUID;

import commerce.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperMeController() {

    @GetMapping("/shopper/me")
    ShopperMeView me(Principal user) {
        UUID id = UUID.fromString(user.getName());
        return new ShopperMeView(id, null, null);
    }
}
