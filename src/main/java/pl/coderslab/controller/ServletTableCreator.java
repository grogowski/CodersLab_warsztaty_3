package pl.coderslab.controller;

import pl.coderslab.utils.DbConnection;
import pl.coderslab.utils.TableCreator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/createtables")
public class ServletTableCreator extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Connection connection = DbConnection.getConnection();
            TableCreator.createTables(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
