package Product.Service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/*
Acts as data transfer object.
it is a good practice not expose or send our entity to outside world. because in the future if we add
some sensitive data memembers that we do not want the out side users to see.
 */
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
