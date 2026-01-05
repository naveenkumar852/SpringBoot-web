package com.telusuko.EcomSpring.Controller; // Defines the package where this controller class is located

import com.telusuko.EcomSpring.model.Product; // Imports Product model class
import com.telusuko.EcomSpring.service.ProductService; // Imports service layer for product logic
import org.springframework.beans.factory.annotation.Autowired; // Enables dependency injection
import org.springframework.http.HttpStatus; // Provides HTTP status codes (200, 404, etc.)
import org.springframework.http.ResponseEntity; // Wraps response body with HTTP status
import org.springframework.web.bind.annotation.*; // Provides REST annotations like @GetMapping, @PostMapping
import org.springframework.web.multipart.MultipartFile; // Handles file uploads
import java.io.IOException; // Handles input-output exceptions
import java.util.List; // Used to store list of products

@RestController // Marks this class as a REST controller (returns JSON responses)
@CrossOrigin(origins = "http://localhost:5173") // Allows requests from frontend running on this URL
@RequestMapping("/api") // Base URL path for all endpoints in this controller
public class ProductController { // Controller class for handling product-related APIs

    @Autowired // Automatically injects ProductService bean
    private ProductService productService; // Service object for business logic

    @GetMapping("/products") // Maps HTTP GET requests to /api/products
    public ResponseEntity<List<Product>> getProducts() { // Method to get all products
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK); // Returns products with 200 OK
    }

    @GetMapping("/product/{id}") // Maps HTTP GET requests with product ID in URL
    public ResponseEntity<Product> getProductById(@PathVariable int id) { // Extracts ID from URL ResponseEntity is a Spring class used to represent the entire HTTP response sent from a controller to a client.  1)Response Body (data) 2)HTTP Status Code 3)TTP Headers
        Product product = productService.getProductById(id); // Fetches product by ID

        if (product != null) // Checks if product exists
            return new ResponseEntity<>(product, HttpStatus.OK); // Returns product with 200 OK
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Returns 404 if product not found
    }

    @PostMapping("/product") // Maps HTTP POST requests to /api/product
    public ResponseEntity<?> addProduct( // Method to add a new product
                                         @RequestPart Product product, // Reads product data from multipart request
                                         @RequestPart MultipartFile imageFile) { // Reads image file from request

        try { // Starts try block for exception handling
            Product savedProduct = productService.addProduct(product, imageFile); // Saves product and image
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED); // Returns saved product with 201 Created
        } catch (IOException e) { // Catches IO exception if image upload fails
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Returns 500 error
        }
    }
}
