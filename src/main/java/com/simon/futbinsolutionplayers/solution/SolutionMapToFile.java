package com.simon.futbinsolutionplayers.solution;
import java.io.*;
import java.util.Map;

public class SolutionMapToFile {

     void writeMapToFile(Map<String, Map<String, Integer>> aMap) throws IOException {
        File file = new File("sbc");
        FileOutputStream f = new FileOutputStream(file);
        ObjectOutputStream s = new ObjectOutputStream(f);
        s.writeObject(aMap);
        s.close();
    }

     Map<String, Map<String, Integer>> readMap(){
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
