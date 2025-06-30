package commerce.api.controller;

import commerce.ShopperRepository;
import commerce.view.ShopperMeView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
public record ShopperMeController(
    ShopperRepository repository
) {

    @GetMapping("/shopper/me")
    public ResponseEntity<ShopperMeView> me(
        Principal user
    ) {
        UUID id = UUID.fromString(user.getName());
        return repository.findById(id)
            .map(ShopperMeView::new)
            .map(ResponseEntity::ok)
            .orElseThrow();
    }
}
