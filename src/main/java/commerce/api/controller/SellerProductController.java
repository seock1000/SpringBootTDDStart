package commerce.api.controller;

import commerce.SellerRepository;
import commerce.command.RegisterProductCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerProductController(
    SellerRepository repository
) {

    @PostMapping("/seller/products")
    public ResponseEntity<?> registerProduct(
        Principal user,
        @RequestBody RegisterProductCommand command
    ) {
        UUID id = UUID.fromString(user.getName());
        if(repository.findById(id).isEmpty()) {
            return ResponseEntity.status(403).build();
        } else if(!isValidUrl(command.imgUri())) {
            return ResponseEntity.badRequest().build();
        }
        URI location = URI.create("/seller/products/" + UUID.randomUUID());
        return ResponseEntity.created(location).build();
    }

    private boolean isValidUrl(String value) {
        try {
            URI uri = URI.create(value);
            return uri.getHost() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
