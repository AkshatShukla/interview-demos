package com.quickbase;

import com.quickbase.devint.ConcreteStatService;
import com.quickbase.devint.DBManager;
import com.quickbase.devint.DBManagerImpl;
import com.quickbase.devint.IStatService;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        if (null == c) {
            System.out.println("Connection Failure.");
            System.exit(1);
        }

        List<Pair<String, Integer>> databaseList = dbm.selectPopulationByCountry(c);
        List<Pair<String, Integer>> concreteList = is.GetCountryPopulations();
        List<Pair<String, Integer>> finalList = combinePopulationLists(databaseList, concreteList);

        // Printing to console code
        System.out.println("\n------Final Merged List:-----");
        Collections.sort(finalList);
        for (Pair<String, Integer> pair : finalList) {
            System.out.println(pair.getLeft() + '\t' + pair.getValue().toString());
        }
        System.out.println("\n");
        System.out.println("Size of list from Database data: " + databaseList.size());
        System.out.println("Size of list from Concrete (API) data: " + concreteList.size());
        System.out.println("Size of final merged list: " + finalList.size());
    }

    /**
     * This method takes in a list of country-population pairs from sql database
     * and a list of country-population pairs from the concrete data or API
     * reference and combines them into one final list of country-population
     * pairs. In the event of duplicate population data for a given country,
     * the data from the sql database is used.
     * @param databaseList - list of country-population pairs from the sql
     *                     database
     * @param concreteList - list of country-population pairs from the API
     *                     reference
     * @return - combined list of country-population pairs made out of lists
     * from sql and API reference without duplicate entries
     */
    private static List<Pair<String, Integer>> combinePopulationLists(List<Pair<String, Integer>> databaseList,
                                                                     List<Pair<String, Integer>> concreteList) {

        List<String> commonCountryNames = new ArrayList<>();
        List<Pair<String, Integer>> output = new ArrayList<>();

        // Add the country names which are common in concrete API data and
        // the database data to a list called commonCountryNames
        for (Pair<String, Integer> pair : concreteList) {
            for (Pair<String, Integer> other : databaseList) {
                if (other.getLeft().equals(pair.getLeft()))
                    commonCountryNames.add(pair.getLeft());
            }
        }

        // Handle United States of America duplicate to U.S.A case and if needed
        // add more exceptions to the condition. This is added to the
        // commonCountries list
        for (Pair<String, Integer> pair : concreteList) {
            if (pair.getLeft().equals("United States of America")) {
                commonCountryNames.add(pair.getLeft());
            }
        }


        // Add the data from concrete list which is uncommon to the database
        // list to the final merged list
        for (Pair<String, Integer> pair : concreteList) {
            if (!commonCountryNames.contains(pair.getLeft())) {
                output.add(pair);
            }
        }

        // Add the data from the database list which is already unique to the
        // final merged list
        output.addAll(databaseList);
        return output;
    }
}