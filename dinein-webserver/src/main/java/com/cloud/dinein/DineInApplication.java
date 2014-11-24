package com.cloud.dinein;

import com.cloud.dinein.cli.RenderCommand;
import com.cloud.dinein.core.Person;
import com.cloud.dinein.core.Template;
import com.cloud.dinein.filter.DateRequiredFeature;
import com.cloud.dinein.health.TemplateHealthCheck;
import com.cloud.dinein.resources.DineInResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;

public class DineInApplication extends Application<DineInConfiguration> {
    public static void main(String[] args) throws Exception {
        new DineInApplication().run(args);
    }

    private final HibernateBundle<DineInConfiguration> hibernateBundle =
            new HibernateBundle<DineInConfiguration>(Person.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(DineInConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "dine-in";
    }

    @Override
    public void initialize(Bootstrap<DineInConfiguration> bootstrap) {
        bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<DineInConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(DineInConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new ViewBundle());
    }

    @Override
    public void run(DineInConfiguration configuration, Environment environment) {
        final Template template = configuration.buildTemplate();

        environment.healthChecks().register("template", new TemplateHealthCheck(template));
        environment.jersey().register(DateRequiredFeature.class);

        environment.jersey().register(new DineInResource());
    }
}
