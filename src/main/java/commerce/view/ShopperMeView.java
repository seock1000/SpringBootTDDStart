package commerce.view;

import commerce.Shopper;

import java.util.UUID;

public record ShopperMeView(
    UUID id,
    String email,
    String username
) {
    public ShopperMeView(Shopper shopper) {
        this(
            shopper.getId(),
            shopper.getEmail(),
            shopper.getUsername()
        );
    }
}
