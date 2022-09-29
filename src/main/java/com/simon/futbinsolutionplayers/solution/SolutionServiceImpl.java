package com.simon.futbinsolutionplayers.solution;

import com.simon.futbinsolutionplayers.player.Detail;
import com.simon.futbinsolutionplayers.player.Player;
import com.simon.futbinsolutionplayers.player.Rarity;
import com.simon.futbinsolutionplayers.player.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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


    public void extractLinksToTheCheapestSbc(){
        getSolutions().forEach(solution -> {
            try {
                Document document = Jsoup.connect(solution.getSolutionUrl())
                        .userAgent("Mozilla/5.0")
                        .get();
                Elements cheapestSquad = document.select(".thead_des + tr");
                String cheapestSquadUrl = cheapestSquad.select(".squad_url").attr("href");
                solution.setCheapest(FUTBIN + cheapestSquadUrl);
                repository.save(solution);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<String, List<Player>> getNonRarePlayers(){
        extractLinksToTheCheapestSbc();
        Map<String, List<Player>> solutionMap = new TreeMap<>();
        getSolutions().forEach( solution -> {
            try {
                Document document = Jsoup.connect(solution.getCheapest())
                        .userAgent("Mozilla/5.0")
                        .get();
                Elements allPlayers = document.select("div[id~=^cardlid[0-9]+[0-1]*]")
                        .select("a.get-tp > div")
                        .select("[data-rare=0]").empty();
                List<Player> players = allPlayers.stream().map(element -> new Player(element.attr("data-player-commom"),
                               getDetailByClassName(Type.class, element.className()), getDetailByClassName(Rarity.class, element.className())))
                        .collect(Collectors.toList());
                solutionMap.put(solution.getName(), players);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return solutionMap;
    }

    private <E extends Enum<E> & Detail> E getDetailByClassName(Class<E> aEnum, String className){
        List<String> classes = List.of(className.trim().split("\\s+"));
        Optional<E> detailName = Stream.of(aEnum.getEnumConstants()).filter(detail -> classes.contains(detail.getName())).findFirst();
        return detailName.get();
    }


    private void displayResult(){
        getNonRarePlayers().forEach((k, v) -> {
            System.out.println("****************");
            System.out.printf("%s\n", k);
            System.out.println("****************");
            v.forEach(System.out::println);
            System.out.println();

        });
    }
    @Scheduled(fixedRate = 60000)
    public void scrapData(){
        displayResult();
    }
}
