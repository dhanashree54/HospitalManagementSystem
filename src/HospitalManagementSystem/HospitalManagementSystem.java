package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem
{
    private static final String url ="jdbc:mysql://localhost:3306/hospital";
    private static final String username ="root";
    private static final String password ="Dhanoo@12";

    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try
        {
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctor doctor = new Doctor(connection);
            while(true)
            {
                System.out.println("Hospital Management System ");
                System.out.println("1. Add Patients");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointments");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice = scanner.nextInt();
                switch(choice)
                {
                    case 1:
                        // Add Patients
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patients
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        // View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointments
                        bookAppointment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("Thank you for using Hospital Management System");
                        return;
                    default:
                        System.out.println("Enter valid choice!!");
                        break;
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner)
    {
        System.out.println("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID = scanner.nextInt();
        System.out.println("Enter Appointment Date(YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientsById(patientID) && doctor.getDoctorsById(doctorID))
        {
            if(checkDoctorAvailability(doctorID,appointmentDate,connection))
            {
                String appointmentQuery = "Insert into Appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try
                {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorID);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0)
                    {
                        System.out.println("Appointment Booked Successfully!");
                    }
                    else
                    {
                        System.out.println("Appointment Booking Failed!");
                    }
                }
                catch(SQLException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Doctor Not Available On This Date!!");
            }
        }
        else
        {
            System.out.println("Either Patient or Doctor doesn't exist!!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorID,String appointmentDate,Connection connection)
    {
        String query = "Select COUNT(*) from Appointments where doctor_id=? and appointment_date=?";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorID);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                int count = resultSet.getInt(1);
                if(count == 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}