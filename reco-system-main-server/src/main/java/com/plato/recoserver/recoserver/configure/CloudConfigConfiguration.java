package com.plato.recoserver.recoserver.configure;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kevin
 * @date 2022-04-22
 */
@Configuration
@EnableApolloConfig({"application", "experiment", "strategy", "model"})
public class CloudConfigConfiguration {
}
