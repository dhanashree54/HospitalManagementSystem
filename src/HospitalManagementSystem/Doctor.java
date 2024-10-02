package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor
{
    private Connection connection;
    public Doctor(Connection connection)
    {
        this.connection = connection;
    }

    public void viewDoctors()
    {
        String query = "select * from Doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctor : ");
            System.out.println("+-----------+-------------------------+----------------------+");
            System.out.println("| Doctor ID | Name                    | Specialization       |");
            System.out.println("+-----------+-------------------------+----------------------+");
            while (resultSet.next()) {
                int ID = resultSet.getInt("id");
                String Name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("|%-11s|%-25s|%-22s|\n", ID, Name, specialization);
                System.out.println("+-----------+-------------------------+----------------------+");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public boolean getDoctorsById(int id)
    {
        String query = "select * from Doctors where id = ?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}