package com.mechanitis.demo;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class DemoService extends Service<DemoConfiguration> {
    public static void main(String[] args) throws Exception {
        new DemoService().run(args);
    }

    @Override
    public void initialize(Bootstrap<DemoConfiguration> bootstrap) {
        bootstrap.setName("hello-world");
    }

    @Override
    public void run(DemoConfiguration configuration,
                    Environment environment) {
        final String template = configuration.getTemplate();
        final String defaultName = configuration.getDefaultName();
        environment.addResource(new DemoResource(template, defaultName));
    }
}