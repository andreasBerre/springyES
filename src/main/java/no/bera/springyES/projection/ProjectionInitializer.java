package no.bera.springyES.projection;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectionInitializer implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    @Resource
    ActorSystem system;

    @Resource
    ActorRef eventStore;


    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Starting projection init");
        final Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(no.bera.springyES.projection.annotations.Projection.class);

        List<InitProjection> projections = new ArrayList<InitProjection>();

        for (final Object annotatedBean : annotatedBeans.values()) {
            final Class<?> projectionClass = annotatedBean.getClass();
            final no.bera.springyES.projection.annotations.Projection annotation = projectionClass.getAnnotation(no.bera.springyES.projection.annotations.Projection.class);
            String aggregate = annotation.value();
            System.out.println("Found projection: " + projectionClass.getName() + ", with aggregate: " + aggregate);

            projections.add(new InitProjection(annotatedBean, aggregate));
        }

        createProjectionInitializer(projections);
    }

    private void createProjectionInitializer(List<InitProjection> projections) {
        system.actorOf(Props.create(ProjectionManager.class, system, eventStore, projections));
    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}