package project2.project2.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import org.mindrot.jbcrypt.BCrypt;

public class FileUtil {
// Path: src/main/java/project2/project2/utils/FileUtil.java
  private static final String DATABASE = "superSecureDb.csv"; // like the secret key, you would not hardcode this in the real world and it would not be a csv file but an SQL database

public static void checkFile(
    String email,
    String password,
    Consumer<String> onSuccess, // this is where the lambda function openLogInView() is passed, a consumer is a function that takes an input and returns nothing eg, a lambda function
    Runnable onFailure // this is where the lambda function promptSignUp() is passed, a runnable is a function that takes no input and returns nothing eg, a lambda function
  ) {
    System.out.println("Checking file...");
    try (BufferedReader reader = new BufferedReader(new FileReader(DATABASE))) {
      String line;
      reader.readLine(); // skip header
      while ((line = reader.readLine()) != null) {
        String[] data = line.split(",");
        String decryptedEmail = EncryptionUtil.decrypt(data[0]);
        boolean passwordMatch = BCrypt.checkpw(password, data[1]);
        
        // System.out.println("Checking email: " + email + " against " + decryptedEmail);
        // System.out.println("Password match: " + passwordMatch);
        
        if (passwordMatch && decryptedEmail.equals(EncryptionUtil.decrypt(email))) {
          System.out.println("Account found");
          onSuccess.accept(data[2]);
          return;
        }
      }
      System.out.println("Account not found");
      onFailure.run();
    } catch (Exception e) {
      //e.printStackTrace();
      System.out.println("Error checking file, most likely does not exist");
      onFailure.run();
    }
  }

  // append encrypted email, hashed password, and encrypted name to file
  public static void createAccount(String email, String password, String name) {
    System.out.println("Creating account...");
    try {
      if (Files.exists(Paths.get(DATABASE))) {
        try (
          BufferedReader reader = new BufferedReader(new FileReader(DATABASE))
        ) {
          String line;
          while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            if (
              EncryptionUtil.decrypt(data[0]).equals(
                EncryptionUtil.decrypt(email)
              )
            ) {
              return;
            }
          }
        }
      }
      try (FileWriter writer = new FileWriter(DATABASE, true)) {
        writer.write("Email, Password, name\n");
        writer.write(
          email + "," + password + "," + EncryptionUtil.encrypt(name) + "\n"
        );
      }
    } catch (Exception e) {
      //e.printStackTrace();
      System.out.println("Error creating account, most likely email already in use");
    }
  }
}
