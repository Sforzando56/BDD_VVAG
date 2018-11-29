package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuEntreeController {

    @FXML
    public void onAdminChoice(){
        Stage adminStage = new Stage();
        adminStage.setTitle("Menu Administrateur");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/MenuAdmin.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        adminStage.setScene(scene);
        adminStage.show();
    }

    @FXML
    public void onUtilisateurChoice(){
        Stage utilisateurStage = new Stage();
        utilisateurStage.setTitle("Menu Utilisateur");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/MenuUtilisateur.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        Scene scene = new Scene(root);
        utilisateurStage.setScene(scene);
        utilisateurStage.show();
    }


}
