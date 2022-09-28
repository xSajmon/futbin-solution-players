package com.simon.futbinsolutionplayers.solution;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SolutionServiceImpl implements SolutionService {

    private SolutionRepository repository;
    public static final String FUTBIN = "https://www.futbin.com/";

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



    public List<String> extractLinksToTheCheapestSbc(){
        List<String> links = new ArrayList<>();
        getSolutions().forEach(solution -> {
            try {
                Document document = Jsoup.connect(solution.getSolutionUrl())
                        .userAgent("Mozilla/5.0")
                        .get();
                Elements cheapestSquad = document.select(".thead_des + tr");
                String cheapestSquadUrl = cheapestSquad.select(".squad_url").attr("href");
                links.add(FUTBIN + cheapestSquadUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return links;
    }

}
