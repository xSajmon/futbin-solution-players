package com.simon.futbinsolutionplayers.solution;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SolutionServiceImpl implements SolutionService {

    private SolutionRepository repository;

    public SolutionServiceImpl(SolutionRepository repository) {
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
