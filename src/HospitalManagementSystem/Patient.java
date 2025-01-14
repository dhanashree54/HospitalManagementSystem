package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient
{
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner)
    {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient()
    {
        System.out.println("Enter Patient Name: ");
        String name=scanner.next();
        System.out.println("Enter Patient Age: ");
        int age=scanner.nextInt();
        System.out.println("Enter Patient Gender: ");
        String gender=scanner.next();

       try
       {
            String query = "insert into Patients(name,age,gender) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectedRows = preparedStatement.executeUpdate();
           if(affectedRows > 0)
           {
               System.out.println("Patient Added Successfully");
           }
           else
           {
               System.out.println("Failed to add patient!!");
           }
       }
       catch(SQLException e)
       {
           e.printStackTrace();
       }
    }

    public void viewPatients() {
        String query = "select * from Patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patient : ");
            System.out.println("+-------------+-------------------------+---------+----------------+");
            System.out.println("| Patients ID | Name                    | Age     | Gender         |");
            System.out.println("+-------------+-------------------------+---------+----------------+");
            while (resultSet.next()) {
                int patientID = resultSet.getInt("id");
                String patientName = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-13s|%-25s|%-9s|%-16s|\n" , patientID , patientName ,age,gender);
                System.out.println("+-------------+-------------------------+---------+----------------+");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

        public boolean getPatientsById(int id)
        {
            String query = "select * from Patients where id = ?";
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
