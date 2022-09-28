package com.simon.futbinsolutionplayers.solution;

import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("solution")
public class SolutionController {

    private SolutionService solutionService;

    public SolutionController(SolutionService solutionService) {
        this.solutionService = solutionService;
    }

    @GetMapping
    public Set<Solution> getSolutions(){
        return solutionService.getSolutions();
    }

    @PostMapping
    public void saveSolution(@RequestBody Solution solution){
        solutionService.addToDatabase(solution);
    }

}
