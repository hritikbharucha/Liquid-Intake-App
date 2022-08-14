package edu.sdccd.cisc191.template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@SpringBootApplication
public class ReadBeverages {
    private static final Logger log = LoggerFactory.getLogger(ReadBeverages.class);
    public static void main(String[] args) {
        SpringApplication.run(ReadBeverages.class);
    }

    //print all beverages saved in database
    @Bean
    public CommandLineRunner findAllVehicles(BeverageRepository beverageRepository) {
        return (args) -> {
            log.info("Beverages found with findAll()");
            log.info("--------------------");
            for (Beverage beverage : beverageRepository.findAll()) {
                log.info(beverage.toString());
            }
            log.info("");
        };
    }
}
