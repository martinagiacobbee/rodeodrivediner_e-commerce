package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.exceptions.ProductAlreadyExistsException;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;


    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContaining(name);

    }

    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    public void addProduct(Product product) throws ProductAlreadyExistsException {
        if ( product.getName() != null && productRepository.existsByName(product.getName()) ) {
            throw new ProductAlreadyExistsException();
        }
        productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.deleteById(product.getProdId());
    }

    public void updateProduct(Product product) {
        if(productRepository.existsById(product.getProdId())){
            productRepository.save(product);
        }
    }

    //restituisce tutti i prodotti ordinati per prezzo
    public Page<Product> getProductsOrderByPrice(int page, int limit, Sort.Direction sortType) {
        Sort sort=Sort.by(sortType,"price");
        Pageable pageable= PageRequest.of(page,limit,sort);
        return productRepository.findAll(pageable);
    }

    //restituisce tutti i prodotti contenenti "name" ordinati per prezzo
    public Page<Product> findProductsByNameAndOrderByPrice(int page,int limit,String productName,Sort.Direction sortType){
        Sort sort=Sort.by(sortType,"price");
        Pageable pageable=PageRequest.of(page,limit,sort);
        return productRepository.findByNameContainingIgnoreCase(productName,pageable);
    }

    /*public Page<Product> getRequestFilters(int page, int limit, String productName, Sort.Direction sortType){
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
    }*/


}
