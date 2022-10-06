package com.simon.futbinsolutionplayers.solution;

import java.util.Set;

public interface SolutionDatabaseService {

    void addToDatabase(Solution solution);

    Set<Solution> getSolutions();

}
