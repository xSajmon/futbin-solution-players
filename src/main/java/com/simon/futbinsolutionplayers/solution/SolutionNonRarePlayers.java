package com.simon.futbinsolutionplayers.solution;

import com.simon.futbinsolutionplayers.player.Detail;
import com.simon.futbinsolutionplayers.player.Player;
import com.simon.futbinsolutionplayers.player.Rarity;
import com.simon.futbinsolutionplayers.player.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SolutionNonRarePlayers extends SolutionService {
    private void getNonRarePlayers(List<Solution> solutionList) {
        Map<String, List<Player>> solutionMap = new HashMap<>();
        solutionList.forEach(solution -> {
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
        countPlayers(solutionMap);
        display(solutionMap);
    }

    private <E extends Enum<E> & Detail> E getDetailByClassName(Class<E> aEnum, String className) {
        List<String> classes = List.of(className.trim().split("\\s+"));
        Optional<E> detailName = Stream.of(aEnum.getEnumConstants()).filter(detail -> classes.contains(detail.getName())).findFirst();
        return detailName.get();
    }

    void display(Map<String, List<Player>> solutionMap){
        solutionMap.forEach((k, v) -> {
            System.out.println("****************");
            System.out.printf("%s\n", k);
            System.out.println("****************");
            v.forEach(System.out::println);
            System.out.println();
            fileService.readMap().get(k).forEach((name, count) -> {
                System.out.println(name + ": " + count);
            });
            System.out.println();
        });
    }
    @Override
    public void update(List<Solution> solutionList) {
        getNonRarePlayers(solutionList);
    }}

