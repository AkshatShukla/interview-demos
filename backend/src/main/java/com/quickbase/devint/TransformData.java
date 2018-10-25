package com.quickbase.devint;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author akshat
 */
public interface TransformData {
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
    public List<Pair<String, Integer>> combinePopulationLists(List<Pair<String, Integer>> databaseList,
                                                              List<Pair<String, Integer>> concreteList);

}
