package org.example.rodeodrivediner_webapp.services;

import org.example.rodeodrivediner_webapp.entities.Cart;
import org.example.rodeodrivediner_webapp.entities.Customer;
import org.example.rodeodrivediner_webapp.entities.Product;
import org.example.rodeodrivediner_webapp.entities.ProductInCart;
import org.example.rodeodrivediner_webapp.repositories.CartRepository;
import org.example.rodeodrivediner_webapp.repositories.CustomerRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductInCartRepository;
import org.example.rodeodrivediner_webapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductInCartRepository productInCartRepository;

    @Transactional(readOnly = true)
    public List<ProductInCart> showAllProductsInCart(Customer customer) {
        if (cartRepository.existsCartByCustomer(customer)) {
            Cart cart = cartRepository.findCartByCustomer(customer);
            return cart.getProdottiInCart();
        }
        return new ArrayList<>();
    }

    @Transactional //di default readOnly è a false
    public ProductInCart addProductInCart(String email, Product prodotto) {
        Customer utente = customerRepository.findByEmail(email);
        Cart carrello = utente.getCart();
        ProductInCart prodottoInCart = null;
        for (ProductInCart cc : carrello.getProdottiInCart()) {
            if (cc.getProducts().getProdId() == prodotto.getProdId()) {
                prodottoInCart = cc;
                break;
            }
        }


        if (prodottoInCart != null) {
            //Nel carrello esiste gia' un carrello prodotto contenente il prodotto
            prodottoInCart.setQuantity(prodottoInCart.getQuantity() + 1);
            ProductInCart newCart = productInCartRepository.save(prodottoInCart);
            carrello.getProdottiInCart().remove(prodottoInCart);//rimuovo quello con quantità sbagliata
            carrello.getProdottiInCart().add(newCart);
            cartRepository.save(carrello);
            return newCart;
        } else {
            prodottoInCart = new ProductInCart();
            prodottoInCart.setCart(carrello);
            prodottoInCart.setProducts(prodotto);
            prodottoInCart.setQuantity(1);
            ProductInCart newCart = productInCartRepository.save(prodottoInCart);
            carrello.getProdottiInCart().add(newCart);
            cartRepository.save(carrello);
            return newCart;
        }
    }


    @Transactional
    public ProductInCart removeProductFromCart(String email, Product prodotto){
        Cart carrello=customerRepository.findByEmail(email).getCart();
        for(ProductInCart pc : carrello.getProdottiInCart()){
            if(pc.getProducts().getProdId()==prodotto.getProdId()){
                carrello.getProdottiInCart().remove(pc);
                productInCartRepository.delete(pc);
                cartRepository.save(carrello);
                return pc;
            }
        }
        //Non verra' mai eseguito
        return null;
    }
}
