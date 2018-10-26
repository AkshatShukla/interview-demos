package com.quickbase.devint;

import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by ckeswani on 9/16/15.
 */
public interface DBManager {
    public Connection getConnection();
    public Map<String, Integer> selectPopulationByCountry(Connection connection);
}
