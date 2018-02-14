package project1_tripati_aftaba;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Common {
    public static File readInputFile(String fileName) {
        return new File(fileName);
    }

    public static HashMap<Integer, ArrayList<Integer>> readTransactions(File file) throws IOException {
        HashMap<Integer, ArrayList<Integer>> hashMap = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String transaction = reader.readLine();
        StringTokenizer tokenizer;
        int TID = 0;

        while(transaction != null) {
            tokenizer = new StringTokenizer(transaction);
            hashMap.put(TID, new ArrayList<>());
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                hashMap.get(TID).add(Integer.parseInt(token));
            }
            TID++;
            transaction = reader.readLine();
        }

        reader.close();

        return hashMap;
    }

    public static HashMap<Integer, ArrayList<Integer>> createChunk(HashMap<Integer, ArrayList<Integer>> source, double chunkSizePercentage) {
        HashMap<Integer, ArrayList<Integer>> chunk = new HashMap<>();
        int chunkSize = ((Double)( source.size() * chunkSizePercentage)).intValue();

        for(int i = 0; i < chunkSize; i++) {
            chunk.put(i, source.get(i));
        }

        return chunk;
    }

}
