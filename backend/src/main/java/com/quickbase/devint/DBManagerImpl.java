package com.quickbase.devint;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This DBManager implementation provides a connection to the database containing population data.
 *
 * Created by ckeswani on 9/16/15.
 */
public class DBManagerImpl implements DBManager {

    private static final String driverName = "org.sqlite.JDBC";
    private static final String connectionUrl = "jdbc:sqlite:resources/data" +
            "/citystatecountry.db";
    private static final String sqlQuery =
            "SELECT Country.CountryName, sum(city.Population) AS Population FROM Country, State, City \n" +
            "WHERE Country.CountryId == State.CountryId AND State.StateId" +
            " == City.StateId GROUP BY Country.CountryName";
    /**
     * This method is used to establish a connection to the database
     * @return - a connection class instance which, if not null, establishes the
     * connection to the given sqlite url
     */
    public Connection getConnection() {
        Connection c = null;
        try {
            Class.forName(driverName);
            c = DriverManager.getConnection(connectionUrl);
            System.out.println("Opened database successfully");

        } catch (ClassNotFoundException cnf) {
            System.out.println("could not load driver");
        } catch (SQLException sqle) {
            System.out.println("sql exception:" + sqle.getStackTrace());
        }
        return c;
    }
    //TODO: Add a method (signature of your choosing) to query the db for population data by country


    /**
     * This method is used to query and get population data by country.
     * @return - a list of immutable pairs with each pair comprising of country
     * name and corresponding population as in the database
     */
    public List<Pair<String, Integer>> selectPopulationByCountry(Connection connection){
        List<Pair<String, Integer>> output = new ArrayList<>();

        try (Connection conn = connection;
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sqlQuery)){

            // loop through the result set
            while (rs.next()) {
                output.add(new ImmutablePair<>(rs.getString("CountryName"),
                        rs.getInt("Population")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

}
