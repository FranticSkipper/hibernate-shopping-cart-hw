package mate.academy.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import mate.academy.util.HibernateUtil;

@Dao
public class TicketDaoImpl implements TicketDao {
    @Override
    public Ticket add(Ticket ticket) {
        EntityTransaction entityTransaction = null;

        try (EntityManager em = HibernateUtil
                .getSessionFactory()
                .openSession()) {
            entityTransaction = em.getTransaction();
            entityTransaction.begin();
            em.persist(ticket);
            entityTransaction.commit();

            return ticket;
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }

            throw new DataProcessingException("Can't add new ticket " + ticket, e);
        }
    }
}
