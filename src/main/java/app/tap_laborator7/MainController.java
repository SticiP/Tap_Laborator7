package app.tap_laborator7;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox PolynomOne;
    @FXML
    public VBox PolynomTwo;
    @FXML
    private Label resultLabel;

    private final HashMap<Integer, Float> polynomial1 = new HashMap<>();
    private final HashMap<Integer, Float> polynomial2 = new HashMap<>();

    private HBox getInputTerm()
    {
        HBox hbox = new HBox(5); // Spacing of 10 between children
        TextField textField = new TextField();
        textField.setMaxWidth(75);
        Button upButton = new Button("↑");
        Button downButton = new Button("↓");
        Button deleteButton = new Button("Delete");

        // Adăugăm acțiuni pentru butoane
        upButton.setOnAction(event -> moveHBoxUp(hbox));
        downButton.setOnAction(event -> moveHBoxDown(hbox));
        deleteButton.setOnAction(event -> removeHBox(hbox));

        hbox.getChildren().addAll(textField, upButton, downButton, deleteButton);

        return hbox;
    }

    private void moveHBoxUp(HBox hbox) {
        int index = getIndexInParent(hbox);
        if (index > 0) {
            VBox parent = (VBox) hbox.getParent();
            Node previousNode = parent.getChildren().get(index - 1);
            if (previousNode instanceof HBox) {
                parent.getChildren().remove(index);
                parent.getChildren().add(index - 1, hbox);
            }
        }
    }

    private void moveHBoxDown(HBox hbox) {
        int index = getIndexInParent(hbox);
        VBox parent = (VBox) hbox.getParent();
        if (index < parent.getChildren().size() - 1) {
            parent.getChildren().remove(index);
            parent.getChildren().add(index + 1, hbox);
        }
    }

    private void removeHBox(HBox hbox) {
        VBox parent = (VBox) hbox.getParent();
        parent.getChildren().remove(hbox);
    }

    private int getIndexInParent(Node node) {
        Parent parent = node.getParent();
        if (parent != null) {
            ObservableList<Node> children = ((VBox) parent).getChildren();
            return children.indexOf(node);
        }
        return -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PolynomOne.getChildren().add(getInputTerm());
        PolynomTwo.getChildren().add(getInputTerm());
    }

    @FXML
    private void handleAddTermOne() {
        PolynomOne.getChildren().add(getInputTerm());
    }

    @FXML
    private void handleAddTermTwo()
    {
        PolynomTwo.getChildren().add(getInputTerm());
    }

    @FXML
    private void handleExecute() {
        addPolynomials();
    }

    @FXML
    private void handleHelp() {
        showAlert("Help", "This is a help message.");
    }

    private void addPolynomials() {
        polynomial1.clear();
        polynomial2.clear();

        addPolynomialTerms(PolynomOne, polynomial1);
        addPolynomialTerms(PolynomTwo, polynomial2);

        HashMap<Integer, Float> result = addPolynomials(polynomial1, polynomial2);

        String res = "(" + polynomialToString(polynomial1) +
                ") + (" + polynomialToString(polynomial2) +
                ") = " + polynomialToString(result);

        resultLabel.setText(res);
    }

    private void addPolynomialTerms(VBox polynomialBox, HashMap<Integer, Float> polynomial) {
        polynomialBox.getChildren().forEach(child -> {
            if (child instanceof HBox hbox) {
                TextField textField = (TextField) hbox.getChildren().getFirst();
                String coefficientText = textField.getText();
                if (!coefficientText.isEmpty()) {
                    float coefficient = Float.parseFloat(coefficientText);
                    polynomial.put(polynomial.size() + 1, coefficient);
                }
            }
        });
    }

    private HashMap<Integer, Float> addPolynomials(HashMap<Integer, Float> poly1, HashMap<Integer, Float> poly2) {
        HashMap<Integer, Float> result = new HashMap<>();
        for (Map.Entry<Integer, Float> entry : poly1.entrySet()) {
            result.put(entry.getKey(), result.getOrDefault(entry.getKey(), (float)0) + entry.getValue());
        }
        for (Map.Entry<Integer, Float> entry : poly2.entrySet()) {
            result.put(entry.getKey(), result.getOrDefault(entry.getKey(), (float)0) + entry.getValue());
        }
        return result;
    }

    private String polynomialToString(HashMap<Integer, Float> polynomial) {
        StringBuilder polynomialString = new StringBuilder();

        boolean firstTerm = true;

        for (Map.Entry<Integer, Float> entry : polynomial.entrySet()) {
            int degree = entry.getKey();
            float coefficient = entry.getValue();

            if (!firstTerm && coefficient > 0) {
                polynomialString.append(" + ");
            }

            if (coefficient != 0) {
                if (coefficient != 1 && coefficient != -1 || degree == 0) {
                    polynomialString.append(String.format("%.2f", coefficient));
                } else if (coefficient == -1) {
                    polynomialString.append("-");
                }

                if (degree > 0) {
                    polynomialString.append("x^").append(degree);
                }
                firstTerm = false;
            }
        }

        if (polynomialString.isEmpty()) {
            polynomialString.append("0");
        }

        return polynomialString.toString();
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
