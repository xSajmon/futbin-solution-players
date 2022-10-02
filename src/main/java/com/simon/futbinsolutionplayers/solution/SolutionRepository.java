package com.simon.futbinsolutionplayers.solution;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SolutionRepository extends CrudRepository<Solution, Long> {

    @Override
    Set<Solution> findAll();
    Solution findByName(String name);
}
