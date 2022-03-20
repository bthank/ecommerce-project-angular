package com.binu.ecommerce.config;

import com.binu.ecommerce.entity.Product;
import com.binu.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] theUnsupportedActions = {HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE};

        // disable HTTP methods for Product: POST, PUT and DELETE --- effectively making this READ ONLY
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metaData, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metaData, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // disable HTTP methods for ProductCategory: POST, PUT and DELETE --- effectively making this READ ONLY
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metaData, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metaData, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // call an internal helper method to help expose the ids
        exposeIds(config);

    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // expose entity ids

        // - get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // - create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // - get the entity types for the entities
        for (EntityType tempEntityType :  entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // - expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
