package com.simon.futbinsolutionplayers;

import com.simon.futbinsolutionplayers.solution.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Demo implements CommandLineRunner {
    private final SolutionExtractor solutionExtractor;

    public Demo(SolutionExtractor solutionExtractor) {
        this.solutionExtractor = solutionExtractor;
    }

    @Override
    public void run(String... args){
        SolutionObserver solutionObserver = new SolutionNonRarePlayers();
        solutionExtractor.solutionPublisher.subscribe(solutionObserver);
    }
}
