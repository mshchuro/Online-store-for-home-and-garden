package org.telran.online_store.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.telran.online_store.AbstractTests;
import org.telran.online_store.dto.AddToCartRequest;
import org.telran.online_store.entity.CartItem;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
public class CartServiceImplTest extends AbstractTests {

    @Test
    public void testAddToCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(2);

        cartService.addToCart(request);

        var cart = cartService.getCart();
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());

        CartItem cartItem = cart.getItems().iterator().next();
        assertEquals(testProduct.getId(), cartItem.getProduct().getId());
        assertEquals(2, cartItem.getQuantity());
    }

    @Test
    public void testRemoveFromCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(2);
        cartService.addToCart(request);

        cartService.removeFromCart(testProduct.getId());

        var cart = cartService.getCart();
        assertNotNull(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    public void testClearCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(1);
        cartService.addToCart(request);

        var cartBefore = cartService.getCart();
        assertFalse(cartBefore.getItems().isEmpty());

        cartService.clearCart();

        var cartAfter = cartService.getCart();
        assertTrue(cartAfter.getItems().isEmpty());
    }

    @Test
    public void testClearCartAfterAddingMultipleItems() {
        AddToCartRequest request1 = new AddToCartRequest();
        request1.setProductId(testProduct.getId());
        request1.setQuantity(2);

        AddToCartRequest request2 = new AddToCartRequest();
        request2.setProductId(testProduct.getId());
        request2.setQuantity(3);

        cartService.addToCart(request1);
        cartService.addToCart(request2);

        var cartBefore = cartService.getCart();
        assertFalse(cartBefore.getItems().isEmpty());

        cartService.clearCart();

        var cartAfter = cartService.getCart();
        assertTrue(cartAfter.getItems().isEmpty());
    }

    @Test
    public void testGetCart() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(testProduct.getId());
        request.setQuantity(3);
        cartService.addToCart(request);

        var cart = cartService.getCart();
        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());

        CartItem cartItem = cart.getItems().iterator().next();
        assertEquals(3, cartItem.getQuantity());
    }
}