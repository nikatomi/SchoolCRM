package by.schoolby.configuration;

import by.schoolby.component.PropertyLoader;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.orm.hibernate4.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DbConfig extends WebMvcConfigurerAdapter {

    @Autowired
    public PropertyLoader loader;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(openSessionInViewInterceptor());
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource1());
        builder.scanPackages("by.schoolby.entity")
                .addProperties(loader.load("hibernate.properties"));

        return builder.buildSessionFactory();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource1() {
        Properties dbProperties = loader.load("database.properties");

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(dbProperties.getProperty("jdbc.driverClassName"));
        ds.setUrl(dbProperties.getProperty("jdbc.url"));
        ds.setUsername(dbProperties.getProperty("jdbc.username"));
        ds.setPassword(dbProperties.getProperty("jdbc.password"));

        return ds;
    }

    @Bean
    public HibernateTransactionManager txManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    @Bean
    public OpenSessionInViewInterceptor openSessionInViewInterceptor(){
        OpenSessionInViewInterceptor openSessionInterceptor = new OpenSessionInViewInterceptor();
        openSessionInterceptor.setSessionFactory(sessionFactory());
        return openSessionInterceptor;
    }
}
