package utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class AlertCreator {
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static float shoxDialogPrix(String nomProduit) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Entrée prix départ");
        dialog.setContentText("Entrez le prix de départ pour : " + nomProduit);

        Optional<String> result = dialog.showAndWait();
        final Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        if (result.isPresent() && StringUtils.isNumeric(result.get())) {
            return Float.parseFloat(result.get());
        }

        return 10;
    }
}
