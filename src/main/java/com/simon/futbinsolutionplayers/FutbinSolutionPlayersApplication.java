package com.simon.futbinsolutionplayers;

import com.simon.futbinsolutionplayers.solution.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class FutbinSolutionPlayersApplication {

    public static void main(String[] args) {
        SpringApplication.run(FutbinSolutionPlayersApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(SolutionRepository repository){
        return args -> {
            List<Solution> solutions = List.of(
                    new Solution("https://www.futbin.com/squad-building-challenges/ALL/20/first-xi?page=1&lowest=pc", "FIRST XI"),
                    new Solution("https://www.futbin.com/squad-building-challenges/ALL/24/around-the-world?page=1&lowest=pc", "Around The World"),
                    new Solution("https://www.futbin.com/squad-building-challenges/ALL/19/the-whole-nine-yards?page=1&lowest=pc", "The Whole Nine Yards"),
                    new Solution("https://www.futbin.com/squad-building-challenges/ALL/23/elite-eight?page=1&lowest=pc", "Elite Eight"));
            if(repository.findAll().isEmpty()) repository.saveAll(solutions);
        };
    }
}
