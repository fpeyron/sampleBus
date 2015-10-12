package fr.boursorama.bus.config;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by florent on 04/10/15.
 */

@Configuration
public class InfinispanConfig {


    /**
     * infinispan Camel Configuration
     */
    @Bean
    public DefaultCacheManager cacheManager() {
        DefaultCacheManager cacheManager = new DefaultCacheManager(true);

        //Cache c = cacheManager.getCache();
        //c.put("hello", "world");

        return cacheManager;
    }
}
