package commerce.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.SellerRepository;
import commerce.command.RegisterProductCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

@RestController
public record SellerProductController(ProductRepository repository) {

    @PostMapping("/seller/products")
    public ResponseEntity<?> registerProduct(
        @RequestBody RegisterProductCommand command
    ) {
        if(!isValidUrl(command.imgUri())) {
            return ResponseEntity.badRequest().build();
        }
        UUID id = UUID.randomUUID();
        var product = new Product();
        product.setId(id);
        repository.save(product);
        URI location = URI.create("/seller/products/" + id);
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

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable("id") UUID id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
