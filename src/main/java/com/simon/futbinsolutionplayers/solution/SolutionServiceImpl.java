package com.simon.futbinsolutionplayers.solution;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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


    @Scheduled(fixedRate = 10000)
    public void getNonRarePlayers(){
        extractLinksToTheCheapestSbc().forEach( link -> {
            try {
                Document document = Jsoup.connect(link)
                        .userAgent("Mozilla/5.0")
                        .get();
                Elements allPlayers = document.select("div[id~=^cardlid[0-9]+[0-1]*]")
                        .select("a.get-tp > div")
                        .select("[data-rare=0]");
                List<String> names = new ArrayList<>();
                allPlayers.forEach(element -> {
                    element.select("*").remove();
                    names.add(element.attr("data-player-commom"));
                });
                System.out.println(names);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
