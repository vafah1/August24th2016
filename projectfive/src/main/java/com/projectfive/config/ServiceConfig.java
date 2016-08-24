package com.projectfive.config;

import com.projectfive.config.options.KnownServices;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * This is the base object that ServiceConfig.conf will deserialize into.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
@Data
public class ServiceConfig {
    private Map<KnownServices, Service> services;
    private List<String> validUserIds;
}
