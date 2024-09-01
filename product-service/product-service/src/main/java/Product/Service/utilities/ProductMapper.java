package Product.Service.utilities;

import Product.Service.dto.ProductResponse;
import Product.Service.model.Product;

public class ProductMapper {
    public static ProductResponse mapProductToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
