package mate.academy.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Dao
public class ShoppingCartDaoImpl extends AbstractDao implements ShoppingCartDao {
    public ShoppingCartDaoImpl(SessionFactory factory) {
        super(factory);
    }

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        EntityTransaction entityTransaction = null;

        try (Session em = this.factory.getCurrentSession()) {
            entityTransaction = em.getTransaction();
            entityTransaction.begin();
            em.save(shoppingCart);
            entityTransaction.commit();

            return shoppingCart;
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            throw new DataProcessingException("Can't add new shoppingCart " + shoppingCart, e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (EntityManager entityManager = this.factory.createEntityManager()) {
            return entityManager.createQuery("from ShoppingCart "
                                    + "WHERE user = :user",
                            ShoppingCart.class)
                    .setParameter("user", user)
                    .getResultList()
                    .stream()
                    .findFirst();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get shoppingCart by user " + user, e);
        }
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        EntityTransaction entityTransaction = null;

        try (EntityManager em = this.factory.createEntityManager()) {
            entityTransaction = em.getTransaction();
            entityTransaction.begin();
            em.merge(shoppingCart);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            throw new DataProcessingException("Can't update shoppingCart " + shoppingCart, e);
        }
    }
}
