package project2.project2.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import org.mindrot.jbcrypt.BCrypt;

public class FileUtil {

  private static final String DATABASE = "superSecureDb.csv";

public static void checkFile(
    String email,
    String password,
    Consumer<String> onSuccess,
    Runnable onFailure
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
