package Product.Service;

import Product.Service.dto.ProductRequest;

import Product.Service.model.Product;
import Product.Service.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.math.BigDecimal;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	// Define a Testcontainers-managed MongoDB instance
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"));

	// Autowire the MockMvc object to simulate HTTP requests
	@Autowired
	private MockMvc mockMvc;

	// Autowire the ObjectMapper to convert between Java objects and JSON
	@Autowired
	private ObjectMapper objectMapper;

	// Autowire the ProductRepository to interact with the database
	@Autowired
	ProductRepository productRepository;

	// Set dynamic properties for the MongoDB connection during tests
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	// A simple test to ensure that the Spring context loads successfully
	@Test
	void contextLoads() {
	}

	// Test to verify that a product can be created successfully
	@Test
	void shouldCreateProduct() throws Exception {
		// Create a product request object
		ProductRequest productRequest = getProductRequest();
		// Convert the product request to a JSON string
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		// Perform a POST request to create the product and expect a 201 Created status
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		// Assert that one product exists in the repository
		Assertions.assertEquals(1, productRepository.findAll().size());
		// Clean up the repository
		productRepository.deleteAll();
		// Assert that the repository is empty after cleanup
		Assertions.assertEquals(0, productRepository.findAll().size());
	}

	// Helper method to create a sample ProductRequest object
	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Note 22")
				.description("This is the latest Note 22")
				.price(BigDecimal.valueOf(1400))
				.build();
	}

	// Test to verify that all products can be retrieved successfully
	@Test
	void shouldGetAllProducts() throws Exception {
		// Create and save a product
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		// Perform a GET request to retrieve all products and expect one product in the response
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.length()").value(1))
				.andExpect(jsonPath("$[0].name").value("Note 22"))
				.andExpect(jsonPath("$[0].description").value("This is the latest Note 22"))
				.andExpect(jsonPath("$[0].price").value(1400));

		// Clean up the repository
		productRepository.deleteAll();
		// Assert that the repository is empty after cleanup
		Assertions.assertEquals(0, productRepository.findAll().size());
	}

	// Test to verify that a product can be retrieved by its ID
	@Test
	void shouldGetProductById() throws Exception {
		// Create and save a product
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		// Retrieve the product by ID and verify the details
		Product createdProduct = productRepository.findAll().get(0); // Get the first (and only) product
		String productId = createdProduct.getId();

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/" + productId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.name").value("Note 22"))
				.andExpect(jsonPath("$.description").value("This is the latest Note 22"))
				.andExpect(jsonPath("$.price").value(1400));

		// Clean up the repository
		productRepository.deleteAll();
		// Assert that the repository is empty after cleanup
		Assertions.assertEquals(0, productRepository.findAll().size());
	}

	// Test to verify that attempting to retrieve a non-existent product returns a 404 status
	@Test
	void shouldReturnNotFoundForInvalidProductId() throws Exception {
		// Attempt to retrieve a product with a non-existing ID and expect a 404 Not Found status
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/nonExistingId")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	// Test to verify that a product can be deleted by its ID
	@Test
	void shouldDeleteProductById() throws Exception {
		// Create and save a product
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		// Retrieve the product by ID and delete it
		Product createdProduct = productRepository.findAll().get(0); // Get the first (and only) product
		String productId = createdProduct.getId();

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/" + productId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Verify that the product has been deleted
		Assertions.assertFalse(productRepository.findById(productId).isPresent());

		// Clean up the repository
		productRepository.deleteAll();
		// Assert that the repository is empty after cleanup
		Assertions.assertEquals(0, productRepository.findAll().size());
	}

	// Test to verify that attempting to delete a non-existent product returns a 404 status
	@Test
	void shouldReturnNotFoundWhenDeletingNonExistentProduct() throws Exception {
		// Attempt to delete a product with a non-existing ID and expect a 404 Not Found status
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/nonExistingId")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	// Test to verify that products can be retrieved by their name
	@Test
	void shouldGetProductsByName() throws Exception {
		// Create and save multiple products with the same name
		ProductRequest productRequest1 = ProductRequest.builder()
				.name("Note 22")
				.description("This is the latest Note 22")
				.price(BigDecimal.valueOf(1400))
				.build();
		ProductRequest productRequest2 = ProductRequest.builder()
				.name("Note 22")
				.description("This is another Note 22")
				.price(BigDecimal.valueOf(1200))
				.build();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productRequest1)))
				.andExpect(status().isCreated());

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productRequest2)))
				.andExpect(status().isCreated());

		// Retrieve products by name and verify the number of products returned
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/name/Note 22")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.length()").value(2)) // Expecting 2 products with the name "Note 22"
				.andExpect(jsonPath("$[0].name").value("Note 22"))
				.andExpect(jsonPath("$[1].name").value("Note 22"));

		// Clean up the repository
		productRepository.deleteAll();
		// Assert that the repository is empty after cleanup
		Assertions.assertEquals(0, productRepository.findAll().size());
	}

	// Test to verify that attempting to retrieve products by a non-existent name returns a 404 status
	@Test
	void shouldReturnNotFoundForNonExistentProductName() throws Exception {
		// Attempt to retrieve products with a name that doesn't exist and expect a 404 Not Found status
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/name/NonExistentName")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
