package com.simon.futbinsolutionplayers.solution;

import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("solution")
public class SolutionController {

    private final SolutionDatabaseService solutionDatabaseService;

    public SolutionController(SolutionDatabaseService solutionDatabaseService) {
        this.solutionDatabaseService = solutionDatabaseService;
    }

    @GetMapping
    public Set<Solution> getSolutions(){
        return solutionDatabaseService.getSolutions();
    }

    @PostMapping
    public void saveSolution(@RequestBody Solution solution){
        solutionDatabaseService.addToDatabase(solution);
    }

}
