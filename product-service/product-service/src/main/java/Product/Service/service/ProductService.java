package Product.Service.service;

import Product.Service.dto.ProductRequest;
import Product.Service.dto.ProductResponse;
import Product.Service.model.Product;
import Product.Service.repository.ProductRepository;
import Product.Service.utilities.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

//    public ProductService(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

    public Product createProduct(ProductRequest productRequest)
    {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Producd with id = {} created", savedProduct.getId());
        return savedProduct;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> ProductMapper.mapProductToProductResponse(product))
                .collect(Collectors.toList());
    }



    public ProductResponse getProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent())
            return ProductMapper.mapProductToProductResponse(product.get());
        return null;
    }

    public ProductResponse deleteProductById(String id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent())
        {
            productRepository.deleteById(id);
            return ProductMapper.mapProductToProductResponse(product.get());
        }
        return null;
    }

    public List<ProductResponse> getProductsByName(String name)
    {

        Optional<List<Product>> products = productRepository.findByName(name);
        System.out.println(name);
        System.out.println(products);
        if(products.isPresent())
        {
            return products.get().stream()
                    .map(ProductMapper::mapProductToProductResponse)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
