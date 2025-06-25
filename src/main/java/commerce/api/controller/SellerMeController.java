package commerce.api.controller;

import commerce.view.SellerMeView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public record SellerMeController() {

    @GetMapping("/seller/me")
    SellerMeView me() {
        return new SellerMeView(UUID.randomUUID(), null, null);
    }
}
