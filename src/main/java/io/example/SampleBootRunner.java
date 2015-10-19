package io.example;

import io.example.config.bulk.BulkDataCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@ComponentScan
@EnableJpaRepositories
public class SampleBootRunner implements CommandLineRunner {

    @Autowired
    private BulkDataCreateService bulkDataCreateService;

	public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleBootRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        bulkDataCreateService.createDoctor(0, 29);
        bulkDataCreateService.createPatient(0, 29);
        bulkDataCreateService.createSchedule();
    }
}