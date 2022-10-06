package com.simon.futbinsolutionplayers.solution;

import org.springframework.stereotype.Service;
import java.util.*;



@Service
public class SolutionDatabaseServiceImpl implements SolutionDatabaseService {
    private final SolutionRepository repository;

    public SolutionDatabaseServiceImpl(SolutionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Solution> getSolutions() {
        return repository.findAll();
    }

    @Override
    public void addToDatabase(Solution solution) {
        repository.save(solution);
    }

}