package commerce.api.controller;

import commerce.ShopperRepository;
import commerce.view.ShopperMeView;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeController(
    PasswordEncoder encoder,
    ShopperRepository repository
) {

    @GetMapping("/shopper/me")
    public ShopperMeView me(
        Principal user
    ) {
        UUID id = UUID.fromString(user.getName());
        return new ShopperMeView(id, null, null);
    }
}
