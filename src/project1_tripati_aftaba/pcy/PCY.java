package project1_tripati_aftaba.pcy;

import project1_tripati_aftaba.Common;

import java.util.*;

public class PCY {
    private int numOfBuckets;
    private int minSupport;
    private int[] frequentPairs;

    private HashMap<Integer, ArrayList<Integer>> transactionList;
    private HashMap<Integer, Integer> candidateList;
    private HashMap<Integer, Integer> frequentItemList1;
    private ArrayList<ArrayList<Integer>> frequentItemSet;

    public PCY() {
        transactionList = new HashMap<>();
        candidateList = new HashMap<>();
        frequentItemList1 = new HashMap<>();
        frequentItemSet = new ArrayList<>();
        minSupport = 1;
    }

    public void prepData(HashMap<Integer, ArrayList<Integer>> transactionList) {
        this.transactionList = transactionList;
    }

    public void setMinSupportPercent(Double minSupportPercent) {
        minSupport =  ((Double)( transactionList.size() * minSupportPercent)).intValue();
    }

    public void prepCandidateList() {
        transactionList.forEach((a,b) -> b.forEach(x -> candidateList.put(x, candidateList.getOrDefault(x, 0) + 1)));
    }

    public void getItemSet1() {
        candidateList.forEach((x, y) -> {if(y >= minSupport) frequentItemList1.put(x,y);});
        numOfBuckets = (frequentItemList1.size() * frequentItemList1.size())/2;
        frequentPairs = new int[numOfBuckets];
    }

    public void getItemSet2() {
        HashMap<HashSet<Integer>, Integer> frequentItemList2 = new HashMap<>();
        Integer[] keyList = frequentItemList1.keySet().toArray(new Integer[0]);

        for(int i = 0; i < keyList.length; i++) {
            for (int j = i + 1; j < keyList.length; j++) {
                frequentItemList2.put(new HashSet<>(Arrays.asList(keyList[i], keyList[j])), 0);
            }
        }

        frequentItemList2.forEach((a, b) -> transactionList.forEach((i, j) -> {
            if(j.containsAll(a)) frequentItemSet.add(new ArrayList<>(a));
        }));
    }

    public void prepHashTable() {

        for (ArrayList<Integer> aFrequentItemSet : frequentItemSet) {
            int hashVal = (aFrequentItemSet.get(0) + aFrequentItemSet.get(1)) % numOfBuckets;
            frequentPairs[hashVal]++;
        }
    }

    public void prepBitmap() {
        for(int i = 0; i < frequentPairs.length; i++) frequentPairs[i] = (i >= minSupport)? 1 : 0;
    }
}
