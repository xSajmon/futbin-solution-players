package com.simon.futbinsolutionplayers.solution;

import java.util.List;
import java.util.Set;

public interface SolutionService {

    void addToDatabase(Solution solution);

    Set<Solution> getSolutions();

}
