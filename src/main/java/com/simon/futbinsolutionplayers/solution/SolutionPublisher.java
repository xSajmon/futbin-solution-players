package com.simon.futbinsolutionplayers.solution;

import java.util.ArrayList;
import java.util.List;


public class SolutionPublisher {
    public List<SolutionObserver> observers;

    public SolutionPublisher(){
        this.observers = new ArrayList<>();
    }
    public void subscribe(SolutionObserver solutionObserver){
        observers.add(solutionObserver);
    }

    public void unsubscribe(SolutionObserver solutionObserver){
        this.observers.remove(solutionObserver);
    }

    public void update(List<Solution> solutions) {
//        System.out.println(observers);
        observers.forEach(observer -> observer.update(solutions));
    }
}
