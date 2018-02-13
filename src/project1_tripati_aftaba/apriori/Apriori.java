package project1_tripati_aftaba.apriori;

import java.util.*;

public class Apriori {
    private int minSupport;
    private HashMap<Integer, ArrayList<Integer>> transactionList;
    private HashMap<Integer, Integer> candidateList;
    private HashMap<Integer, Integer> frequentItemList1;
    private HashMap<Set<Integer>, Integer> frequentItemList2;

    public Apriori() {
        transactionList = new HashMap<>();
        candidateList = new HashMap<>();
        frequentItemList1 = new HashMap<>();
        frequentItemList2 = new HashMap<>();
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

    public void getItemSet1() { candidateList.forEach((x, y) -> {if(y >= minSupport) frequentItemList1.put(x,y);}); }

    public void getItemSet2() {
        Integer[] keyList = frequentItemList1.keySet().toArray(new Integer[0]);

        for(int i = 0; i < keyList.length; i++) {
            for (int j = i + 1; j < keyList.length; j++) {
                frequentItemList2.put(new HashSet<>(Arrays.asList(keyList[i], keyList[j])), 0);
            }
        }

        frequentItemList2.forEach((a, b) -> transactionList.forEach((i, j) -> {
            if(j.containsAll(a)) frequentItemList2.put(a, frequentItemList2.get(a) + 1);
        }));


        HashMap<Set<Integer>, Integer> filteredList = new HashMap<>();
        frequentItemList2.forEach((a, b) -> { if(b >= minSupport) filteredList.put(a, b); });
        frequentItemList2 = filteredList;
    }
}
