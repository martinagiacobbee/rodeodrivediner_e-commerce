package org.example.rodeodrivediner_webapp.services;

import jakarta.persistence.EntityManager;
import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.entities.ProductInCart;
import org.example.rodeodrivediner_webapp.exceptions.DateWrongRangeException;
import org.example.rodeodrivediner_webapp.exceptions.IllegalQuantityException;
import org.example.rodeodrivediner_webapp.exceptions.PriceChangedException;
import org.example.rodeodrivediner_webapp.exceptions.UserNotFoundException;
import org.example.rodeodrivediner_webapp.repositories.CartRepository;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductInCartRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = false)
    public Cart addPurchase(Cart cart) throws IllegalQuantityException {
        Cart result = cartRepository.save(cart);
        for ( ProductInCart pic : result.getProductsInCart() ) {
            pic.setCart(result);
            ProductInCart justAdded = productInCartRepository.save(pic);
            entityManager.refresh(justAdded);

            Product product = justAdded.getProduct();

            if (pic.getQuantity() < 0 ) {
                throw new IllegalQuantityException();
            }
            product.setQuantity(pic.getQuantity());
            entityManager.refresh(pic);
        }
        entityManager.refresh(result);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Cart> getPurchasesByUser(Customer cust) throws UserNotFoundException {
        if ( !customerRepository.existsById(cust.getCustId()) ) {
            throw new UserNotFoundException();
        }
        return cartRepository.findByCust(cust);
    }

    @Transactional(readOnly = true)
    public List<Cart> getPurchasesByUserInPeriod( Customer cust, Date startDate, Date endDate) throws UserNotFoundException, DateWrongRangeException {
        if ( !customerRepository.existsById(cust.getCustId()) ) {
            throw new UserNotFoundException();
        }
        if ( startDate.compareTo(endDate) >= 0 ) {
            throw new DateWrongRangeException();
        }
        return cartRepository.findByCustomerInPeriod(startDate, endDate, cust);
    }

    @Transactional(readOnly = true)
    public List<Cart> getAllPurchases() {
        return cartRepository.findAll();
    }


/*
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = {IllegalQuantityException.class, PriceChangedException.class})
    public void crea(List<ProductInCart> items, String email) throws IllegalQuantityException, PriceChangedException{
        Customer cust = null;
        List<Customer> customerList = customerRepository.findByEmail(email);

        for(Customer c : customerList){
            if(c.equals()){}
        }

        Cart cart = new Cart();
        cart.setCust(cust);
        cart.setPurchaseTime(new Date(System.currentTimeMillis()));
        cartRepository.save(cart);

        for(ProductInCart pdc : items){
            Optional<Product> op = productRepository.findById(pdc.getProdInCartId());
            if(!op.isPresent()) throw new RuntimeException();
            Product p = op.get();

            if(p.getQuantity()< pdc.getQuantity())
                throw new IllegalQuantityException(p);
            if(p.getPrice()!=pdc.getPrice())
                throw new PriceChangedException(p);

            ProductInCart productInCart = new ProductInCart();
            productInCart.setProduct(p);
            productInCart.setPrice(pdc.getPrice());
            productInCart.setQuantity(pdc.getQuantity());
            productInCartRepository.save(productInCart);
            cart.getProductsInCart().add(productInCart);
            p.setQuantity(p.getQuantity()-productInCart.getQuantity());
        }
    }

   /* @Transactional(readOnly = true)
    public List<ProductInCart> showAllProductsInCart(Customer customer) {
        if (cartRepository.existsCartByCustomer(customer)) {
            Cart cart = cartRepository.findCartByCustomer(customer);
            return cart.getProductsInCart();
        }
        return new ArrayList<>();
    }


    @Transactional(readOnly = false)
    public ProductInCart addProductInCart(String email, Product prodotto) {
        Customer utente = customerRepository.findByEmail(email);
        Cart carrello = utente.getCart();
        ProductInCart prodottoInCart = null;
        for (ProductInCart cc : carrello.getProductsInCart()) {
            if (cc.getProduct().getProdId() == prodotto.getProdId()) {
                prodottoInCart = cc;
                prodottoInCart.setQuantity(prodottoInCart.getQuantity() + 1);
                ProductInCart addedProd = productInCartRepository.save(prodottoInCart);
                carrello.getProductsInCart().remove(prodottoInCart);//rimuovo quello con quantità sbagliata
                carrello.getProductsInCart().add(addedProd);
                cartRepository.save(carrello);
                return addedProd;
                //l'oggetto è già presente nel carrello
            }
        }
        //il prodotto non era presente nel carrello, lo aggiungo
            prodottoInCart = new ProductInCart();
            prodottoInCart.setCart(carrello);
            prodottoInCart.setProduct(prodotto);
            prodottoInCart.setQuantity(1);
            ProductInCart newCart = productInCartRepository.save(prodottoInCart);
            carrello.getProductsInCart().add(newCart);
            cartRepository.save(carrello);
            return newCart;
    }


    @Transactional
    public ProductInCart removeProductFromCart(String email, Product prodotto){
        Cart carrello=customerRepository.findByEmail(email).getCart();
        for(ProductInCart pc : carrello.getProductsInCart()){
            if(pc.getProduct().getProdId()==prodotto.getProdId()){
                carrello.getProductsInCart().remove(pc);
                productInCartRepository.delete(pc);
                cartRepository.save(carrello);
                return pc;
            }
        }
        //Non verra' mai eseguito
        return null;
    }*/
}
