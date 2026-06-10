package mate.academy.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mate.academy.dao.TicketDao;
import mate.academy.exception.DataProcessingException;
import mate.academy.lib.Dao;
import mate.academy.model.Ticket;
import org.hibernate.SessionFactory;

@Dao
public class TicketDaoImpl extends AbstractDao implements TicketDao {
    public TicketDaoImpl(SessionFactory factory) {
        super(factory);
    }

    @Override
    public Ticket add(Ticket ticket) {
        EntityTransaction entityTransaction = null;

        try (EntityManager em = this.factory.createEntityManager()) {
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
