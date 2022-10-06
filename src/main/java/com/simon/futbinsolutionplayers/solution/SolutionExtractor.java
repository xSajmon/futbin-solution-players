package com.simon.futbinsolutionplayers.solution;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SolutionExtractor {
    public static final String FUTBIN = "https://www.futbin.com/";
    public SolutionPublisher solutionPublisher;
    private final SolutionDatabaseService solutionDatabaseService;


    public SolutionExtractor(SolutionDatabaseService solutionDatabaseService) {
        solutionPublisher = new SolutionPublisher();
        this.solutionDatabaseService = solutionDatabaseService;
    }

    @Scheduled(fixedRate = 10000)
    public void extractLinksToTheCheapestSbc() {
        List<Solution> cheapestSolutionList = new ArrayList<>();
        solutionDatabaseService.getSolutions().forEach(solution -> {
            try {
                Document document = Jsoup.connect(solution.getSolutionUrl())
                        .userAgent("Mozilla/5.0")
                        .get();
                Elements cheapestSquad = document.select(".thead_des + tr");
                Elements secondSquad = cheapestSquad.next();
                String cheapestSquadUrl = cheapestSquad.select(".squad_url").attr("href");
                int difference = getSolutionPriceFromColumns(cheapestSquad) - getSolutionPriceFromColumns(secondSquad);
                Optional<String> currentCheapestSolution = Optional.ofNullable(solution.getCheapest());
                currentCheapestSolution.ifPresentOrElse(x -> {
                    if (!x.equals(FUTBIN + cheapestSquadUrl) && difference < 50) {
                        setSolutionAsCheapest(solution, cheapestSquadUrl);
                        cheapestSolutionList.add(solution);
                    }
                }, () -> {
                    setSolutionAsCheapest(solution, cheapestSquadUrl);
                    cheapestSolutionList.add(solution);
                } );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        if(!cheapestSolutionList.isEmpty()){
            solutionPublisher.update(cheapestSolutionList);
        } else {
            System.out.println("Lack of new solutions");
        }
    }

    public int getSolutionPriceFromColumns(Elements element){
        return Integer.parseInt(element.select("td").get(6).text().replace(",", ""));
    }

    void setSolutionAsCheapest(Solution solution, String url){
        solution.setCheapest(FUTBIN + url);
        solutionDatabaseService.addToDatabase(solution);
    }
}
