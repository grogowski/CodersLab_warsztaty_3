package pl.coderslab.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Exercise {
    private int id;
    private String title;
    private String description;

    public Exercise() {

    }

    public Exercise(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Exercise[] loadAllExercises(Connection connection) throws SQLException {
        ArrayList<Exercise> exercises = new ArrayList<>();
        String sql = "SELECT * FROM exercise";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            exercises.add(getExerciseFromResultSet(resultSet));
        }
        Exercise[] eArray = new Exercise[exercises.size()];
        eArray = exercises.toArray(eArray);
        return eArray;
    }

    public static Exercise loadExerciseById(Connection connection, int id) throws SQLException {
        String sql = "SELECT * FROM exercise WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return getExerciseFromResultSet(resultSet);
        }
        return null;
    }

    public void delete(Connection connection) throws SQLException {
        if (this.id != 0) {
            String deleteExercise = "DELETE FROM exercise WHERE id=?";
            PreparedStatement exerciseStatement = connection.prepareStatement(deleteExercise);
            exerciseStatement.setInt(1, this.id);
            exerciseStatement.executeUpdate();
            this.id = 0;
        }
    }

    public void saveToDB(Connection connection) throws SQLException {
        if (this.id == 0) {
            String sql = "INSERT INTO exercise(title, description) VALUES (?, ?)";
            String[] generatedColumns = {"ID"};
            PreparedStatement preparedStatement = connection.prepareStatement(sql, generatedColumns);
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.description);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                this.id = rs.getInt(1);
            }
        } else {
            String sql = "UPDATE exercise SET title=?, description=? where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, this.title);
            preparedStatement.setString(2, this.description);
            preparedStatement.setInt(3, this.id);
            preparedStatement.executeUpdate();
        }
    }

    public static Exercise getExerciseFromResultSet(ResultSet resultSet) throws SQLException {
        Exercise loadedExercise = new Exercise();
        loadedExercise.id = resultSet.getInt("id");
        loadedExercise.title = resultSet.getString("title");
        loadedExercise.description = resultSet.getString("description");
        return loadedExercise;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
