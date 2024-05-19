package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    private List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    private List<Product> getProductsByName(String name) {
        List<Product> prodotti = productRepository.findByName(name);
        return prodotti;
    }

    private Product addProduct(Product product) {
        return productRepository.save(product);
    }

    private void deleteProduct(Product product) {
        productRepository.deleteById(product.getProdId());
    }

    private void updateProduct(Product product) {
        if(productRepository.existsById(product.getProdId())){
            productRepository.save(product);
        }
    }

    private Page<Product> getProductsOrderByPrice(int page, int limit, Sort.Direction sortType) {
        Sort sort=Sort.by(sortType,"price");
        Pageable pageable= PageRequest.of(page,limit,sort);
        return productRepository.findAll(pageable);
    }

    private Page<Product> findProductByName(int page,int limit,String productName){
        Pageable pageable= PageRequest.of(page,limit);
        return productRepository.findByNameContainingIgnoreCase(productName,pageable);
    }

    private Page<Product> getProductsList(int page,int limit){
        Pageable pageable= PageRequest.of(page,limit);
        return productRepository.findAll(pageable);
    }

    private Page<Product> findProductsByNameAndOrderByPrice(int page,int limit,String productName,Sort.Direction sortType){
        Sort sort=Sort.by(sortType,"price");
        Pageable pageable=PageRequest.of(page,limit,sort);
        return productRepository.findByNameContainingIgnoreCase(productName,pageable);
    }

    public Page<Product> getRequestFilters(int page, int limit, String productName, Sort.Direction sortType){
        Page<Product> productPage=null;
        if(productName==null && sortType==null){
            productPage =getProductsList(page,limit);
        }
        if(productName!=null && sortType==null){
            productPage = findProductByName(page,limit,productName);
        }
        if(productName==null && sortType!=null){
            productPage = getProductsOrderByPrice(page,limit,sortType);
        }
        if(productName!=null && sortType!=null){
            productPage =findProductsByNameAndOrderByPrice(page,limit,productName,sortType);
        }
        return  productPage;
    }


}
