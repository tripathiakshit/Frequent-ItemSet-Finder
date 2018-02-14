package project1_tripati_aftaba;

import project1_tripati_aftaba.apriori.Apriori;
import project1_tripati_aftaba.apriori.AprioriProcessDebug;
import project1_tripati_aftaba.pcy.PCY;
import project1_tripati_aftaba.pcy.PCYProcessDebug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        final Double[] supportList = new Double[] {0.01, 0.05, 0.1};
        final Double[] chunkSizes = new Double[] {0.01, 0.05, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        long lStartTime, lEndTime;
        HashMap<Integer, ArrayList<Integer>> transactionList;
        File dataSet;

        // Get file path and read file.
        if(args.length > 1) {
            dataSet = new File(args[1]);
        }
        else {
            Scanner s = new Scanner(System.in);
            System.out.print("Enter file path: ");
            // Sample input: D:\Onedrive\College\425\Project 1\retail.txt
            String dataSetPath = s.nextLine();
            dataSet = Common.readInputFile(dataSetPath);
            s.close();
        }

        transactionList = Common.readTransactions(dataSet);

        /*
        // Verbose run of APriori (Support: 5%, Chunk: 100%)
        AprioriProcessDebug apd = new AprioriProcessDebug();
        apd.prepData(transactionList);
        apd.setMinSupportPercent(0.05);
        apd.prepCandidateList();
        apd.getItemSet1();
        apd.getItemSet2();

        // Verbose run of PCY (Support: 5%, Chunk: 100%)
        PCYProcessDebug ppd = new PCYProcessDebug();
        ppd.prepData(transactionList);
        ppd.setMinSupportPercent(0.05);
        ppd.prepCandidateList();
        ppd.getItemSet1();
        ppd.getItemSet2();
        ppd.prepHashTable();
        ppd.prepBitmap();
        */

        Apriori apriori = new Apriori();
        PCY pcy = new PCY();

        System.out.println("Project 1 - Akshit Tripathi(Apriori) and Ayub Khan(PCY)\n" +
                "=======================================================");
        for(Double support : supportList) {
            System.out.println("\nSupport: " + support * 100 + "%\n-------------");
            for(Double chunk : chunkSizes) {

                // Calculate Run Time for Apriori
                lStartTime = System.nanoTime();
                apriori.prepData(Common.createChunk(transactionList, chunk));
                apriori.setMinSupportPercent(support);
                apriori.prepCandidateList();
                apriori.getItemSet1();
                apriori.getItemSet2();
                lEndTime = System.nanoTime();
                System.out.print("Run time: " + (lEndTime - lStartTime) / 1000000 + "ms (AP) / ");

                // Calculate Run Time for PCY
                lStartTime = System.nanoTime();
                pcy.prepData(Common.createChunk(transactionList, chunk));
                pcy.setMinSupportPercent(support);
                pcy.prepCandidateList();
                pcy.getItemSet1();
                pcy.getItemSet2();
                pcy.prepHashTable();
                pcy.prepBitmap();
                lEndTime = System.nanoTime();
                System.out.println((lEndTime - lStartTime) / 1000000 + "ms (PCY) [" + chunk * 100 + "% chunk]");

                //Reinitialize variables before calling them again (resets data structures in classes)
                apriori = new Apriori();
                pcy = new PCY();
            }
        }
    }
}
