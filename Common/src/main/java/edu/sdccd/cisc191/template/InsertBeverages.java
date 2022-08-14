package edu.sdccd.cisc191.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

import java.time.LocalDate;

@SpringBootApplication
public class InsertBeverages {

    private static final Logger log = LoggerFactory.getLogger(InsertBeverages.class);

    public static void main(String[] args) {
        SpringApplication.run(InsertBeverages.class);
    }

    // save Beverages to the database using beverage repository
    @Bean
    public CommandLineRunner demo(BeverageRepository repository) {
        return (args) -> {
            LocalDate date = LocalDate.now();
            // save a few beverages
            repository.save(new Beverage(10, "oz", "Soda", date));
            repository.save(new Beverage(250, "mL", "Water", date));
        };
    }
}
