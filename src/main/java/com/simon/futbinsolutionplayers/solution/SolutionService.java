package com.simon.futbinsolutionplayers.solution;

import com.simon.futbinsolutionplayers.player.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


abstract class SolutionService implements SolutionObserver{

     final SolutionMapToFile fileService = new SolutionMapToFile();

    public void countPlayers(Map<String, List<Player>> aMap) {
        Map<String, Map<String, Integer>> finalMap = new HashMap<>(fileService.readMap());
        aMap.forEach((k, v) -> {
            Map<String, Integer> playersCount = finalMap.getOrDefault(k, new HashMap<>());
            v.forEach(player -> {
                playersCount.computeIfPresent(player.getName(), (name, count) -> count+1);
                playersCount.putIfAbsent(player.getName(), 1);
            });
            finalMap.put(k, playersCount);
            try {
                fileService.writeMapToFile(finalMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
