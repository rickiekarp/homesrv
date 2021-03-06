package net.rickiekarp.homeserver.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.env.Environment
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transactionManager",
        basePackages = ["net.rickiekarp.foundation.data.repository"]
)
@EnableRedisRepositories(basePackages = ["net.rickiekarp.foundation.config.redis"])
open class HomeDatabaseConfig {

    @Autowired
    private val env: Environment? = null

    /**
     * DataSource definition for database connection. Settings are read from
     * the application.yml file (using the env object).
     */
    @Primary
    @Bean(name = ["dataSource"])
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env!!.getProperty("db.driver")!!)
        dataSource.url = env.getProperty("db.url")
        dataSource.username = env.getProperty("db.username")
        dataSource.password = env.getProperty("db.password")
        return dataSource
    }

    @Primary
    @Bean(name = ["entityManager"])
    open fun entityManagerFactory(builder: EntityManagerFactoryBuilder, @Qualifier("dataSource") dataSource: DataSource): LocalContainerEntityManagerFactoryBean {

        return builder
                .dataSource(dataSource)
                .packages("net.rickiekarp.homeserver.config")
                .persistenceUnit("applicationPU")
                .build()
    }

    @Primary
    @Bean(name = ["transactionManager"])
    open fun transactionManager(@Qualifier("entityManager") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }

    /**
     * PersistenceExceptionTranslationPostProcessor is a bean post processor
     * which adds an advisor to any bean annotated with Repository so that any
     * platform-specific exceptions are caught and then rethrown as one
     * Spring's unchecked data access exceptions (i.e. a subclass of
     * DataAccessException).
     */
    @Primary
    @Bean(name = ["exceptionTranslation"])
    open fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor {
        return PersistenceExceptionTranslationPostProcessor()
    }
}