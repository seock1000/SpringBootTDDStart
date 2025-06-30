package commerce.api.controller;

import commerce.view.ShopperMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeController() {

    @GetMapping("/shopper/me")
    public ShopperMeView me() {
        UUID id = UUID.randomUUID();
        return new ShopperMeView(id, null, null);
    }
}
