package com.quickbase;

import com.quickbase.devint.*;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.util.*;

/**
 * The main method of the executable JAR generated from this repository. This is to let you
 * execute something from the command-line or IDE for the purposes of demonstration, but you can choose
 * to demonstrate in a different way (e.g. if you're using a framework)
 */
public class Main {
    public static void main(String args[]) {
        System.out.println("Starting.");
        System.out.print("Getting DB Connection...");

        DBManager dbm = new DBManagerImpl();
        Connection c = dbm.getConnection();
        IStatService is = new ConcreteStatService();
        TransformData transformData = new TransformDataImpl();
        if (null == c) {
            System.out.println("Connection Failure.");
            System.exit(1);
        }

        Map<String, Integer> databaseList = dbm.selectPopulationByCountry(c);
        List<Pair<String, Integer>> concreteList = is.GetCountryPopulations();

        System.out.println("Size of list from Database data: " + databaseList.size());
        System.out.println("Size of list from Concrete (API) data: " + concreteList.size());

        List<Pair<String, Integer>> finalList =
                transformData.combinePopulationLists(databaseList,concreteList);

        // Printing to console code
        System.out.println("\n------Final Merged List:-----");
        Collections.sort(finalList);
        for (Pair<String, Integer> pair : finalList) {
            System.out.println(pair.getLeft() + '\t' + pair.getValue().toString());
        }
        System.out.println("\n");
        System.out.println("Size of final merged list: " + finalList.size());
    }
}