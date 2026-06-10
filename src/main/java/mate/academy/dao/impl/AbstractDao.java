package mate.academy.dao.impl;

import org.hibernate.SessionFactory;

public class AbstractDao {
    protected final SessionFactory factory;

    public AbstractDao(SessionFactory factory) {
        this.factory = factory;
    }
}
