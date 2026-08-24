package com.example;

import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    /**
     * DataSource real. Se construye con initializeDataSourceBuilder() en lugar de
     * DataSourceBuilder.create() para que respete la configuracion de Hikari
     * (spring.datasource.hikari.*) ademas de url/usuario/password.
     */
    @Bean
    public DataSource realDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    /**
     * Envoltorio de log4jdbc que registra las sentencias SQL ejecutadas.
     * El @Qualifier evita depender de que el nombre del parametro sobreviva a la
     * compilacion para resolver cual de los dos beans DataSource se inyecta.
     */
    @Bean
    @Primary
    public DataSource dataSource(@Qualifier("realDataSource") DataSource realDataSource) {
        return new DataSourceSpy(realDataSource);
    }
}
