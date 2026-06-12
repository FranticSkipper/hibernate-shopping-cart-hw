package mate.academy.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {
    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        EntityTransaction entityTransaction = null;

        try (Session em = HibernateUtil
                .getSessionFactory()
                .openSession()) {
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
        try (EntityManager entityManager = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ShoppingCart> criteriaQuery = criteriaBuilder
                    .createQuery(ShoppingCart.class);
            Root<ShoppingCart> root = criteriaQuery.from(ShoppingCart.class);
            root.fetch("tickets", JoinType.LEFT);
            Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);
            criteriaQuery.where(userPredicate);

            return entityManager
                    .createQuery(criteriaQuery)
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

        try (EntityManager em = HibernateUtil
                .getSessionFactory()
                .openSession()) {
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
