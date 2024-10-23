package com.mattoi.frambo_study;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        if (isLaunchArgumentProvided(args, "run-initial-data")) {
            String initScript = "classpath:inital_data.sql";
            jdbcTemplate.execute(initScript);
            System.out.println("Initial data script executed successfully.");
        }
    }

    private boolean isLaunchArgumentProvided(String[] args, String argument) {
        for (String arg : args) {
            if (arg.equalsIgnoreCase(argument)) {
                return true;
            }
        }
        return false;
    }
}