package com.cloud.dinein.health;

import com.cloud.dinein.core.Template;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Optional;

public class TemplateHealthCheck extends HealthCheck {
    private final Template template;

    public TemplateHealthCheck(Template template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        template.render(Optional.of("woo"));
        template.render(Optional.<String>absent());
        return Result.healthy();
    }
}
