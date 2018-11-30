package pl.coderslab.model;


import pl.coderslab.utils.DbConnection;
import pl.coderslab.utils.TableCreator;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            Connection connection = DbConnection.getConnection();
            TableCreator.createTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
