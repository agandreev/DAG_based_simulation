/**
 * @author <a href="mailto:agandreev@edu.hse.ru"> Arkadiy Andreev</a>
 */

package dag_simulation.forms_components;

import javafx.scene.control.Alert;

/**
 * Represents info box class.
 */
public class AboutBox {
    /**
     * Displays developer's info.
     */
    public static void display(String exceptionMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Runtime error");
        alert.setHeaderText("Error info:");
        alert.setContentText(exceptionMessage);

        alert.showAndWait();
    }
}
