package project2.project2.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.mindrot.jbcrypt.BCrypt;

public class FileUtil {

  //////////////////////////////////////////////////////////////////// table paths

  static final private String USERS_TABLE = "database/users.csv";
  static final private String COURSES_TABLE = "database/courses.csv";
  static final private String GRADES_TABLE = "database/grades.csv";
  static final private String ASSSIGNMENTS_TABLE = "database/assignments.csv";

  //////////////////////////////////////////////////////////////////// check input credentials agaisnt database and either login, or prompt to sign up

  public static void logInCheck(
    String email,
    String password,
    Consumer<String> onSuccess,
    Runnable onFailure
  ) {
    System.out.println("Checking file...");
    List<String[]> results = select(2, email, USERS_TABLE); // Use select to find rows with matching email
    if (results.isEmpty()) {
      System.out.println("Account not found");
      onFailure.run();
      return;
    }

    String[] userData = results.get(0); // Get the first matching row (should only be one but select returns a list)
    boolean passwordMatch = BCrypt.checkpw(password, userData[3]); // Check hashed password

    if (passwordMatch) {
      System.out.println("Account found");
      onSuccess.accept(userData[1]); // Pass the user's name to onSuccess
    } else {
      System.out.println("Incorrect password");
      onFailure.run(); // password does not match -> prompt to sign up
    }
  }

  //////////////////////////////////////////////////////////////////// check if file exists, if not create it

  public static void initilizeTables() {
    try {
      Files.createDirectories(Paths.get("database"));
    } catch (IOException e) {
      System.out.println("Error creating database directory"); // if this fails and there is no database directory, the rest will fail
      e.printStackTrace();
    }

    try {
      FileWriter writer = new FileWriter(USERS_TABLE);
      writer.write("Id, Name, Email, Password, IsProfessor\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Error creating users table");
      e.printStackTrace();
    }

    try {
      FileWriter writer = new FileWriter(COURSES_TABLE);
      writer.write("Course_Id, Course_Name, Professor_Id\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Error creating courses table");
      e.printStackTrace();
    }

    try {
      FileWriter writer = new FileWriter(GRADES_TABLE);
      writer.write("Grade_Id, User_Id, Course_Id, Assignment_Id, Grade\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Error creating grades table");
      e.printStackTrace();
    }

    try {
      FileWriter writer = new FileWriter(ASSSIGNMENTS_TABLE);
      writer.write("Assignment_Id, Assignment_Name\n");
      writer.close();
    } catch (IOException e) {
      System.out.println("Error creating assignments table");
      e.printStackTrace();
    }
  }

  //////////////////////////////////////////////////////////////////// create account, ensuring no duplicate emails and incrementing id

  public static void createAccount(
    String name,
    String email,
    String password,
    boolean isProfessor
  ) {
    if (Files.exists(Paths.get(USERS_TABLE))) { // check if file exists, if not -> initialize database
      System.out.println("File exists");
    } else {
      System.out.println("File does not exist");
      initilizeTables();
    }

    // check if email already exists and also get next id
    // if it does, return
    // if it doesn't, create account
    int id = 0;
    try (
      BufferedReader reader = new BufferedReader(new FileReader(USERS_TABLE))
    ) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[2].equals(email)) {
          System.out.println("Email already exists");
          return;
        }
      }
    } catch (Exception e) {
      System.out.println("Error checking file");
      e.printStackTrace();
    }

    String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

    insert(
      new String[] { name, email, hashedPassword, String.valueOf(isProfessor) },
      USERS_TABLE
    );
  }

  //////////////////////////////////////////////////////////////////// insert, delete, select database methods

  public static void insert(String[] data, String table) {
    try (FileWriter writer = new FileWriter(table, true)) {
      // get next id
      int id = 0;
      try (BufferedReader reader = new BufferedReader(new FileReader(table))) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] row = line.split(",");
          try {
            id = Integer.parseInt(row[0]);
            id++;
          } catch (NumberFormatException e) {
            id = 1;
          }
        }
      } catch (Exception e) {
        System.out.println("Error inserting data");
        e.printStackTrace();
      }

      // add id to data
      String[] datawithId = new String[data.length + 1];

      for (int i = 1; i < datawithId.length; i++) {
        datawithId[i] = data[i - 1];
      }

      writer.write(String.join(",", datawithId) + "\n");
    } catch (Exception e) {
      System.out.println("Error inserting data");
      e.printStackTrace();
    }
  }

  // delete a row from the table with the given id

  public static void delete(String id, String table) throws IOException {
    String temp = "temp.csv";
    try (
      BufferedReader reader = new BufferedReader(new FileReader(table));
      FileWriter writer = new FileWriter("temp.csv")
    ) {
      reader.readLine(); // skip header

      String line;
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[0].equals(id)) {
          continue;
        } else {
          writer.write(line + "\n");
        }
      }
    }

    try {
      Files.delete(Paths.get(table));
      Files.move(Paths.get(temp), Paths.get(table));
    } catch (IOException e) {
      System.out.println("Error deleting file");
      e.printStackTrace();
    }
  }

  // select data from table, returns a list of row arrays
  // col is the column to search, input is the value to search for
  // for instance, if you want to select all rows with the same course id, col would be 2 and input would be the course id

  public static List<String[]> select(int col, String input, String table) {
    List<String[]> results = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(table))) {
      String line;
      reader.readLine(); // skip header
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        if (data[col].equals(input)) {
          results.add(data);
          return results;
        }
      }
    } catch (Exception e) {
      System.out.println("Error selecting data");
      e.printStackTrace();
    }
    return results;
  }
}
