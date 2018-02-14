package project1_tripati_aftaba.pcy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PCYProcessDebug {
    private int numOfBuckets;
    private int minSupport;
    private int[] frequentPairs;

    private HashMap<Integer, ArrayList<Integer>> transactionList;
    private HashMap<Integer, Integer> candidateList;
    private HashMap<Integer, Integer> frequentItemList1;
    private ArrayList<ArrayList<Integer>> frequentItemSet;

    public PCYProcessDebug() {
        transactionList = new HashMap<>();
        candidateList = new HashMap<>();
        frequentItemList1 = new HashMap<>();
        frequentItemSet = new ArrayList<>();
        minSupport = 1;
    }

    public void prepData(HashMap<Integer, ArrayList<Integer>> transactionList) {
        this.transactionList = transactionList;
        System.out.println("Done.");
    }

    public void setMinSupportPercent(Double minSupportPercent) {
        System.out.println("\nSetting support value percentage to " + (minSupportPercent * 100) + "%. New support value is " + minSupport);
        minSupport =  ((Double)( transactionList.size() * minSupportPercent)).intValue();
    }

    public void prepCandidateList() {
        System.out.print("\nPreparing candidate list... ");
        transactionList.forEach((a,b) -> b.forEach(x -> candidateList.put(x, candidateList.getOrDefault(x, 0) + 1)));
        System.out.println("Done.");

    }

    public void getItemSet1() {
        System.out.print("\nPreparing Frequent ItemSet 1... ");
        candidateList.forEach((x, y) -> {if(y >= minSupport) frequentItemList1.put(x,y);});
        numOfBuckets = (frequentItemList1.size() * frequentItemList1.size())/2;
        frequentPairs = new int[numOfBuckets];
        System.out.println("Prepared. Removed items with less than " + minSupport + " frequency.");
    }

    public void getItemSet2() {
        System.out.println("\nPreparing Frequent ItemSet 2...");

        System.out.print("\tStep 1: Put HashMap keys into array... ");
        HashMap<HashSet<Integer>, Integer> frequentItemList2 = new HashMap<>();
        Integer[] keyList = frequentItemList1.keySet().toArray(new Integer[0]);

        for(int i = 0; i < keyList.length; i++) {
            for (int j = i + 1; j < keyList.length; j++) {
                frequentItemList2.put(new HashSet<>(Arrays.asList(keyList[i], keyList[j])), 0);
            }
        }
        System.out.println("Done. Frequent ItemList 2 is ready.");

        System.out.print("\tStep 3: Find frequency of item sets in transactions...");

        frequentItemList2.forEach((a, b) -> transactionList.forEach((i, j) -> {
            if(j.containsAll(a)) frequentItemSet.add(new ArrayList<>(a));
        }));
        System.out.println("Done. Frequent ItemList 2 now contains pairs that are above support threshold");
    }

    public void prepHashTable() {
        System.out.println("Preparing Hash Table...");
        for (ArrayList<Integer> aFrequentItemSet : frequentItemSet) {
            int hashVal = (aFrequentItemSet.get(0) + aFrequentItemSet.get(1)) % numOfBuckets;
            frequentPairs[hashVal]++;
        }
        System.out.println("Done");
    }

    public void prepBitmap() {
        System.out.println("Preparing Bit Map...");
        for(int i = 0; i < frequentPairs.length; i++) frequentPairs[i] = (i >= minSupport)? 1 : 0;
        System.out.println("Done. Converted frequent pair array to a bitmap.");
    }
}
