package org.example.rodeodrivediner_webapp.services;

import jakarta.persistence.EntityManager;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.entities.ProductInPurchase;
import org.example.rodeodrivediner_webapp.entities.Purchase;
import org.example.rodeodrivediner_webapp.exceptions.DateWrongRangeException;
import org.example.rodeodrivediner_webapp.exceptions.IllegalQuantityException;
import org.example.rodeodrivediner_webapp.exceptions.UserNotFoundException;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductInPurchaseRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.example.rodeodrivediner_webapp.repositories.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductInPurchaseRepository productInPurchaseRepository;

    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Purchase addPurchase(Purchase purchase) throws IllegalQuantityException {
        Purchase newPurchase = purchaseRepository.save(purchase);
        for (ProductInPurchase prodBuyed : newPurchase.getProductsInPurchase() ) {
            prodBuyed.setPurchase(newPurchase);
            ProductInPurchase justAdded = productInPurchaseRepository.save(prodBuyed);
            entityManager.refresh(justAdded);

            Product requestedProd = justAdded.getProdotto();
            int qta = requestedProd.getQuantity();

            if ( qta < 0 ) {
                throw new IllegalQuantityException();
            }
        }
        entityManager.refresh(newPurchase);
        return newPurchase;
    }


    @Transactional(readOnly = false, rollbackFor = IllegalArgumentException.class)
    public ProductInPurchase addPurchase(Product prod, String email, int qta) throws IllegalArgumentException {
        Customer cust = customerRepository.findByEmail(email);
        Purchase purchase = new Purchase();
        purchase.setCustomer(cust);

        ProductInPurchase prodInPurchase = new ProductInPurchase();
        prodInPurchase.setProdotto(prod);
        prodInPurchase.setPurchase(purchase);
        prodInPurchase.setQuantity(qta);
        ProductInPurchase newProdInPurchase = productInPurchaseRepository.save(prodInPurchase);

        purchase.getProductsInPurchase().add(newProdInPurchase);//lo aggiungo nella lista dei miei acquisti
        Purchase purchaseNew = purchaseRepository.save(purchase);
        cust.getPurchases().add(purchaseNew);
        customerRepository.save(cust);

        Product p = productRepository.findByProdId(prod.getProdId());//mi prendo il prodotto, se il prezzo è diverso allora l'ha modificato e non posso fare l'acquisto

        if (p.getPrice() != prod.getPrice())
            throw new IllegalArgumentException();
        return newProdInPurchase;
    }


    @Transactional(readOnly = true)
    public List<ProductInPurchase> getPurchasesByCustomer(String email){
        List<Purchase> purchases = customerRepository.findByEmail(email).getPurchases();
        List<ProductInPurchase> ret=new ArrayList<>();
        for(Purchase p : purchases){
            for(ProductInPurchase cp : p.getProductsInPurchase()){
                ret.add(cp);
            }
        }
        return ret;
    }

    @Transactional(readOnly = true)
    public List<Purchase> getPurchasesByCustomerInPeriod(Customer cust, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException {
        if ( !customerRepository.existsById(cust.getCustId()) ) {
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) >= 0 ) {
            throw new DateWrongRangeException();
        }
        return purchaseRepository.findByCustomerInPeriod(startDate, endDate, cust);
    }

    @Transactional(readOnly = true)
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }
}
