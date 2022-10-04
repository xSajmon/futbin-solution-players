package com.simon.futbinsolutionplayers.solution;

import com.simon.futbinsolutionplayers.player.Detail;
import com.simon.futbinsolutionplayers.player.Player;
import com.simon.futbinsolutionplayers.player.Rarity;
import com.simon.futbinsolutionplayers.player.Type;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class SolutionServiceImpl implements SolutionService {

    public static final String FUTBIN = "https://www.futbin.com/";
    private SolutionRepository repository;

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


    public List<Solution> extractLinksToTheCheapestSbc() {
        List<Solution> cheapestSolutionList = new ArrayList<>();
        getSolutions().forEach(solution -> {
            try {
                Document document = Jsoup.connect(solution.getSolutionUrl())
                        .userAgent("Mozilla/5.0")
                        .get();
                Elements cheapestSquad = document.select(".thead_des + tr");
                Elements secondSquad = cheapestSquad.next();
                String cheapestSquadUrl = cheapestSquad.select(".squad_url").attr("href");
                int difference = getSolutionPriceFromColumns(cheapestSquad) - getSolutionPriceFromColumns(secondSquad);
                System.out.println(difference);
                Optional<String>  currentCheapestSolution = Optional.ofNullable(solution.getCheapest());
                currentCheapestSolution.ifPresentOrElse(x -> {
                    if (!x.equals(FUTBIN + cheapestSquadUrl) && difference < 0) {
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
        return cheapestSolutionList;
    }

    public int getSolutionPriceFromColumns(Elements element){
        return Integer.parseInt(element.select("td").get(6).text().replace(",", ""));
    }

    void setSolutionAsCheapest(Solution solution, String url){
        solution.setCheapest(FUTBIN + url);
        addToDatabase(solution);
    }
    private Map<String, List<Player>> getNonRarePlayers() {
        Map<String, List<Player>> solutionMap = new HashMap<>();
        extractLinksToTheCheapestSbc().forEach(solution -> {
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
        try {
            countPlayers(solutionMap);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return solutionMap;
    }

    private <E extends Enum<E> & Detail> E getDetailByClassName(Class<E> aEnum, String className) {
        List<String> classes = List.of(className.trim().split("\\s+"));
        Optional<E> detailName = Stream.of(aEnum.getEnumConstants()).filter(detail -> classes.contains(detail.getName())).findFirst();
        return detailName.get();
    }


    private void displayResult() {
        getNonRarePlayers().forEach((k, v) -> {
            System.out.println("****************");
            System.out.printf("%s\n", k);
            System.out.println("****************");
            v.forEach(System.out::println);
            System.out.println();

            readMap().get(k).forEach((name, count) -> {
                System.out.println(name + ": " + count);
            });
            System.out.println();

        });
    }

    @Scheduled(fixedRate = 30000)
    public void scrapData() throws IOException {
        if(!new File("sbc").exists()){
            writeMapToFile(Map.of());
        }
        displayResult();
    }


    public void countPlayers(Map<String, List<Player>> aMap) throws IOException, ClassNotFoundException {
        Map<String, Map<String, Integer>> finalMap = new HashMap<>(readMap());
        aMap.forEach((k, v) -> {
            Map<String, Integer> playersCount = finalMap.getOrDefault(k, new HashMap<>());
            v.forEach(player -> {
                playersCount.computeIfPresent(player.getName(), (name, count) -> count+1);
                playersCount.putIfAbsent(player.getName(), 1);
            });
            finalMap.put(k, playersCount);
            try {
                writeMapToFile(finalMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void writeMapToFile(Map<String, Map<String, Integer>> aMap) throws IOException {
        File file = new File("sbc");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(aMap);
        s.close();
    }

    private Map<String, Map<String, Integer>> readMap(){
        File file = new File("sbc");
        try{
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            Map<String, Map<String, Integer>> obj = (Map<String, Map<String, Integer>>)  s.readObject();
            s.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}