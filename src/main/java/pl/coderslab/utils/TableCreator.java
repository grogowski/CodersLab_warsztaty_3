package pl.coderslab.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreator {

    public static void createTables(Connection connection) throws SQLException {
        String query1 = "create table user_group (id int auto_increment, name varchar(255), primary key(id))";
        String query2 = "create table users (id bigint auto_increment, username varchar(255), email varchar(255) unique, " +
                "password varchar(245), user_group_id int, primary key(id), " +
                "foreign key (user_group_id) references user_group(id) on delete cascade)";
        String query3 = "create table exercise (id int auto_increment, title varchar(255), description text, " +
                "primary key(id))";
        String query4 = "create table solution (id int auto_increment, created datetime, updated datetime, description text, " +
                "exercise_id int, users_id bigint, primary key(id), foreign key(exercise_id) references exercise(id) on delete cascade, " +
                "foreign key (users_id) references users(id) on delete cascade)";
        String[] queries = {query1, query2, query3, query4};
        for (String s:queries) {
            PreparedStatement statement = connection.prepareStatement(s);
            statement.executeUpdate();
        }
    }
}
