package project1_tripati_aftaba.apriori;

import project1_tripati_aftaba.Common;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AprioriProcessDebug {
    private int minSupport;
    private Double minSupportPercent;

    // Individual items filtered through support
    private HashMap<Integer, ArrayList<Integer>> transactionList;
    private HashMap<Integer, Integer> candidateList;
    private HashMap<Integer, Integer> frequentItemList1;
    private HashMap<Set<Integer>, Integer> frequentItemList2;

    public AprioriProcessDebug() {
        transactionList = new HashMap<>();
        candidateList = new HashMap<>();
        frequentItemList1 = new HashMap<>();
        frequentItemList2 = new HashMap<>();
        minSupport = 1;
    }

    /*

     * <p>
     * This function will read the file and store each transaction in
     * the hashMap transactionList.
     *
     * This list can then be read to find
     * total number of items, or it can be used to create a candidate
     * list of a given support value.
     * </p>
     *
     * @param File   A file containing the data to be processed
     *
     * @throws IOException

     */
    public void prepData(HashMap<Integer, ArrayList<Integer>> transactionList) throws IOException {
        this.transactionList = transactionList;
//        transactionList.forEach((a,b) -> System.out.println("TID " + a + ": " + b));
        System.out.println("Done.");
    }

    public void setMinSupport(int minSupport) {
        System.out.println("\nSetting support value to: " + minSupport + " (was: " + this.minSupport + ").");
        this.minSupport = minSupport;
    }

    public void setMinSupportPercent(Double minSupportPercent) {
        minSupport =  ((Double)( transactionList.size() * minSupportPercent)).intValue();
        this.minSupportPercent = minSupportPercent * 100;
        System.out.println("\nSetting support value percentage to " + (minSupportPercent * 100) + "%. New support value is " + minSupport);
    }

    // Count the frequency of every item across all transactions
    public void prepCandidateList() {
        System.out.print("\nPreparing candidate list... ");
        transactionList.forEach((a,b) -> b.forEach(x -> candidateList.put(x, candidateList.getOrDefault(x, 0) + 1)));
        System.out.println("Done.");
//        candidateList.forEach((a,b) -> System.out.println("Item " + a + ": " + b));
    }

    // Filter out items where support < minSupport
    public void getItemSet1() {
        System.out.print("\nPreparing Frequent ItemSet 1... ");
        candidateList.forEach((x, y) -> {if(y >= minSupport) frequentItemList1.put(x,y);});
        System.out.println("Prepared. Removed items with less than " + minSupport + " frequency.");
//        candidateList1.forEach((a,b) -> System.out.println("Item: " + a + ", count: " + b));
    }

    public void getItemSet2() {
        System.out.println("\nPreparing Frequent ItemSet 2...");

        System.out.print("\tStep 1: Put HashMap keys into array... ");
        Integer[] keyList = frequentItemList1.keySet().toArray(new Integer[0]);
        System.out.println("Done.");

        System.out.print("\tStep 2: Use array to generate permutations...");
        for(int i = 0; i < keyList.length; i++) {
            for (int j = i + 1; j < keyList.length; j++) {
                frequentItemList2.put(new HashSet<>(Arrays.asList(keyList[i], keyList[j])), 0);
            }
        }
//        frequentItemList2.forEach((x, y) -> System.out.println(x + " --> " + y));
        System.out.println("Done. Frequent ItemList 2 is ready.");

        System.out.print("\tStep 3: Find frequency of item sets in transactions...");
        frequentItemList2.forEach((a, b) -> {
            transactionList.forEach((i, j) -> {
                if(j.containsAll(a)) frequentItemList2.put(a, frequentItemList2.get(a) + 1);
            });
        });
//        frequentItemList2.forEach((x, y) -> System.out.println(x + " --> " + y));
        System.out.println("Done.");

        System.out.print("\tStep 4: Remove items that do not meet support criteria... ");
        HashMap<Set<Integer>, Integer> filteredList = new HashMap<>();
        frequentItemList2.forEach((a, b) -> {
            if(b >= minSupport) filteredList.put(a, b);
        });
        frequentItemList2 = filteredList;
//        frequentItemList2.forEach((x, y) -> System.out.println(x + " --> " + y));
        System.out.println("Done.");

        System.out.println("\nDone. Frequent ItemList 2 now contains pairs that are above support threshold.");
        System.out.println("\nRun Finished.");

        System.out.print("Final Item set (" + minSupportPercent + "% support):\n+---------+---------+\n|  Pairs  |Frequency|\n+---------+---------+\n");
        frequentItemList2.forEach((x, y) -> System.out.println("|" + x + " | " + y + "\t|"));
        System.out.println("+---------+---------+");
    }

}
