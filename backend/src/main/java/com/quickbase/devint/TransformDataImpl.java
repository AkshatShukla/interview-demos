package com.quickbase.devint;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This TransformData implementation is used to merge two lists of
 * country-population pair objects from database and API reference without
 * duplicates.
 */
public class TransformDataImpl implements TransformData {

    /**
     * This method takes in a list of country-population pairs from sql database
     * and a list of country-population pairs from the concrete data or API
     * reference and combines them into one final list of country-population
     * pairs. In the event of duplicate population data for a given country,
     * the data from the sql database is used.
     *
     * @param databaseList - list of country-population pairs from the sql
     *                     database
     * @param concreteList - list of country-population pairs from the API
     *                     reference
     * @return - combined list of country-population pairs made out of lists
     * from sql and API reference without duplicate entries
     */
    @Override
    public List<Pair<String, Integer>> combinePopulationLists(List<Pair<String, Integer>> databaseList, List<Pair<String, Integer>> concreteList) {
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