/**
 * The config package contains the logic ({@link com.projectfive.config.ConfigLoader}) and objects to deserialize the
 * TypeSafe config ".conf" file into Java objects.
 *
 * <p>Any class that needs to access the config data will call {@link com.projectfive.config.ConfigLoader#getConfig()
 * ConfigLoader.getConfig()} which will return the cached config object.
 *
 * <p>The remaining classes in this package will be the domain objects matching the ".conf" files structure.
 *
 * @author <a href="mailto:JGraham@aimconsulting.com">Justin Graham</a>
 * @since 2/15/16
 */
package com.projectfive.config;