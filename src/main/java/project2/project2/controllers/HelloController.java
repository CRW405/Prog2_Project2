// package project2.project2.controllers;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Scene;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.PasswordField;
// import javafx.scene.control.TextField;
// import javafx.stage.Stage;
// import project2.project2.utils.EncryptionUtil;
// import project2.project2.utils.FileUtil;
// import project2.project2.utils.HashingUtil;

// public class HelloController {
//   // fxml elements
//   @FXML
//   private TextField emailField;
//   @FXML
//   private PasswordField passwordField;
//   @FXML
//   private Button close;
//   //

//   // sign in button clicked -> get email and password -> encrypt email -> hash
//   // password -> pass to checkFile
//   // if file exists -> open log in view
//   // else -> prompt sign up
//   @FXML
//   private void signIn(ActionEvent event) {
//     System.out.println("Attempting login, checking file...");
//     String email = this.emailField.getText();
//     String password = this.passwordField.getText();
//     String encryptedEmail = EncryptionUtil.encrypt(email);
//     String hashedPassword = HashingUtil.hashPassword(password);

//     // checks if file exists
//     // if it does -> open log in view
//     // else -> prompt sign up

//     // this is an example of a lambda expression which is a way to pass a function
//     // as a parameter. Syntax: (parameter/input) -> function logic

//     // also an example of an anonymous function which is just a function without a
//     // name, you don't need a name cause the function is being passed as a parameter
//     FileUtil.checkFile(encryptedEmail, password, this::openLogInView, () -> promptSignUp(null));
//   }
//   //

//   // sign up button clicked -> open sign up view
//   @FXML
//   private void promptSignUp(ActionEvent event) {
//     System.out.println("Prompting sign up...");
//     try {
//       FXMLLoader loader = new FXMLLoader(
//           getClass().getResource("/project2/project2/signUp-view.fxml"));
//       Stage stage = new Stage();
//       stage.setScene(new Scene(loader.load()));
//       stage.setTitle("Sign Up");
//       stage.show();
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//   }
//   //

//   // open log in view with name passed in
//   private void openLogInView(String name) {
//     System.out.println("opening log in view");
//     try {
//       FXMLLoader loader = new FXMLLoader(
//           getClass().getResource("/project2/project2/logIn-view.fxml"));
//       Stage stage = new Stage();
//       stage.setScene(new Scene(loader.load()));
//       stage.setTitle("Log In");

//       // get the label from the log in view and set the text to the decrypted name
//       Label nameLabel = (Label) loader.getNamespace().get("nameLabel");
//       nameLabel.setText(EncryptionUtil.decrypt(name));
//       stage.show();
//     } catch (Exception e) {
//       e.printStackTrace();
//     }
//   }
//   //

//   @FXML
//   private void close(ActionEvent event) {
//     Stage stage = (Stage) this.close.getScene().getWindow();
//     stage.close();
//   }
// }
