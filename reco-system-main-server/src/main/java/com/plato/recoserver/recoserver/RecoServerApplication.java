package com.plato.recoserver.recoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Kevin
 * @date 2022-03-02
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class RecoServerApplication {

    public static void main(String[] args) {
//        Metrics.startSlf4jReporter(60, TimeUnit.SECONDS);
        SpringApplication.run(RecoServerApplication.class, args);
    }
}
