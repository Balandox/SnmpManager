package org.suai.snmp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.suai.snmp.models.Metrics;

import java.util.List;

public class MetricsDAO {

    private final SessionFactory sessionFactory;

    public MetricsDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Metrics> getAll(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("SELECT m FROM Metrics m", Metrics.class)
                .getResultList();
    }

    @Transactional
    public void save(Metrics metrics){
        Session session = sessionFactory.getCurrentSession();
        session.save(metrics);
    }

    @Transactional
    public void update(int id, Metrics newMetrics){
        Session session = sessionFactory.getCurrentSession();
        Metrics metricsToBeUpdated = session.get(Metrics.class, id);

        metricsToBeUpdated.setSysUpTime(newMetrics.getSysUpTime());
        metricsToBeUpdated.setIfOutErrors(newMetrics.getIfOutErrors());
        metricsToBeUpdated.setIfInDiscards(newMetrics.getIfInDiscards());
        metricsToBeUpdated.setIfOperStatus(newMetrics.getIfOperStatus());
        metricsToBeUpdated.setIfLastChange(newMetrics.getIfLastChange());
    }

    @Transactional
    public void delete(int id){
        Session session = sessionFactory.getCurrentSession();
        session.delete(session.get(Metrics.class, id));
    }

}
