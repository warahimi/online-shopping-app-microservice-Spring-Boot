package Product.Service.controller;

import Product.Service.dto.ProductRequest;
import Product.Service.dto.ProductResponse;
import Product.Service.model.Product;
import Product.Service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest)
    {
        Product product = productService.createProduct(productRequest);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts()
    {
        List<ProductResponse> allProducts = productService.getAllProducts();
        return new ResponseEntity<>(allProducts,HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id)
    {
        ProductResponse product = productService.getProductById(id);
        if(product != null)
        {
            return new ResponseEntity<>(product, HttpStatus.FOUND);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable String id)
    {
        ProductResponse productResponse = productService.deleteProductById(id);
        if(productResponse != null)
        {
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@PathVariable String name)
    {
        List<ProductResponse> products = productService.getProductsByName(name);
        if(products != null)
        {
            return new ResponseEntity<>(products,HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
