package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.entities.summary.Product;
import kz.symtech.antifraud.coreservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {
    //Product saveProduct(Product product);

     List<Product> getAllProducts();

     Optional<Product> getProductById(Long id);
     void deleteProductById(Long id);

    Product createProduct(Product product);
}
