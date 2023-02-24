package org.suai.snmp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.suai.snmp.dao.MetricsDAO;
import org.suai.snmp.models.Metrics;
import org.suai.snmp.models.SnmpManager;
import org.suai.snmp.tasks.SaveTask;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@ComponentScan("org.suai.snmp")
@PropertySource("classpath:hibernate.properties")
@EnableTransactionManagement
public class SpringConfig{

    private final ApplicationContext applicationContext;

    private final Environment env;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }


    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("hibernate.driver_class"));
        dataSource.setUrl(env.getRequiredProperty("hibernate.connection.url"));
        dataSource.setUsername(env.getRequiredProperty("hibernate.connection.username"));
        dataSource.setPassword(env.getRequiredProperty("hibernate.connection.password"));

        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));

        return properties;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setAnnotatedClasses(Metrics.class);

        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }

    @Bean(name = "scheduled")
    public ScheduledExecutorService scheduledExecutorService(){
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Bean(name = "metricsDAO")
    public MetricsDAO metricsDAO(){
        return new MetricsDAO((sessionFactory().getObject()));
    }

    @Bean(name = "snmpManager")
    public SnmpManager snmpManager() throws IOException {
        SnmpManager snmpManager = new SnmpManager();
        snmpManager.start();
        return snmpManager;
    }

    @Bean(name = "saveTask")
    public SaveTask saveTask() throws IOException {
        return new SaveTask(metricsDAO(), snmpManager());
    }
}
