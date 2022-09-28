package com.simon.futbinsolutionplayers.solution;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String solutionUrl;

    public Solution() {

    }

    public Solution(Long id, String solutionUrl) {
        this.id = id;
        this.solutionUrl = solutionUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSolutionUrl() {
        return solutionUrl;
    }

    public void setSolutionUrl(String solutionUrl) {
        this.solutionUrl = solutionUrl;
    }
}
