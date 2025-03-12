package commerce.api.controller;

import java.net.URI;
import java.util.UUID;

import commerce.Product;
import commerce.ProductRepository;
import commerce.command.RegisterProductCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record SellerProductsController(ProductRepository repository) {

    @PostMapping("/seller/products")
    ResponseEntity<?> registerProduct(
        @RequestBody RegisterProductCommand command
    ) {
        if (isValidUri(command.imageUri()) == false) {
            return ResponseEntity.badRequest().build();
        }

        UUID id = UUID.randomUUID();
        var product = new Product();
        product.setId(id);
        repository.save(product);
        URI location = URI.create("/seller/products/" + id);
        return ResponseEntity.created(location).build();
    }

    private boolean isValidUri(String value) {
        try {
            URI uri = URI.create(value);
            return uri.getHost() != null;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    @GetMapping("/seller/products/{id}")
    ResponseEntity<?> findProduct(@PathVariable UUID id) {
        return repository
            .findById(id)
            .map(product -> ResponseEntity.ok().build())
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
