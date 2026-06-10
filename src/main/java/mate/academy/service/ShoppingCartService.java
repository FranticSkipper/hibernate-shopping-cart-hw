package mate.academy.service;

import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public interface ShoppingCartService {
    /**
     * This method is responsible for adding a Ticket to the ShoppingCart
     * @param movieSession contains the information required for the ticket
     * @param user - the User who wants to buy the ticket for a specific movieSession
     */
    void addSession(MovieSession movieSession, User user);

    ShoppingCart getByUser(User user);

    void registerNewShoppingCart(User user);

    void clear(ShoppingCart shoppingCart);
}
