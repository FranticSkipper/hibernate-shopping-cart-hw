package mate.academy.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.MovieSession;
import mate.academy.model.ShoppingCart;
import mate.academy.model.Ticket;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Inject
    private ShoppingCartDao shoppingCartDao;

    @Inject
    private AuthenticationService authenticationService;

    @Override
    public void addSession(MovieSession movieSession, User user) {
        Optional<ShoppingCart> shoppingCartOptional = this.shoppingCartDao.getByUser(user);

        if (shoppingCartOptional.isEmpty()) {
            throw new RuntimeException("Can't add session "
                    + movieSession
                    + ". Shopping cart is not exist for this user " + user);
        }

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMovieSession(movieSession);

        ShoppingCart shoppingCart = shoppingCartOptional.get();
        List<Ticket> tickets = shoppingCart.getTickets();
        tickets.add(ticket);
        this.shoppingCartDao.update(shoppingCart);
    }

    @Override
    public ShoppingCart getByUser(User user) {
        return this.shoppingCartDao.getByUser(user).orElse(null);
    }

    @Override
    public void registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());

        this.shoppingCartDao.add(shoppingCart);
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        shoppingCart.setTickets(new LinkedList<>());
        this.shoppingCartDao.update(shoppingCart);
    }
}
