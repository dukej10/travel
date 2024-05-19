package com.dukez.best_travel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * Esta clase se encarga de leer los archivos de propiedades de la aplicación.
 */
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:configs/api_currency.properties"),
        @PropertySource(value = "classpath:configs/redis.properties"),

})
public class PropertiesConfig {

}
