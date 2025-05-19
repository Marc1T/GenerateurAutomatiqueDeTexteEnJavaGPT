package generateurAutomatiqueTexteGPT;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe principale de l'application JavaFX pour générer des emails professionnels via l'API OpenAI.
 * Fournit une interface graphique avec des champs pour le contexte, le destinataire, l'objet, le ton,
 * et des boutons pour générer, effacer, et copier l'email.
 */
public class EmailGeneratorApp extends Application {
    private final OpenAiHandler openAiHandler;

    public EmailGeneratorApp() {
        try {
            this.openAiHandler = new OpenAiHandler();
        } catch (IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            throw e;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Composants de l’interface
        Label contextLabel = new Label("Contexte de l'email :");
        TextField contextField = new TextField();
        contextField.setPromptText("Ex. : demander une prolongation pour un devoir");

        Label recipientLabel = new Label("Destinataire :");
        TextField recipientField = new TextField();
        recipientField.setPromptText("Ex. : Professeur Dupont");

        Label subjectLabel = new Label("Objet de l'email :");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Ex. : Demande de prolongation");

        Label toneLabel = new Label("Ton de l'email :");
        ComboBox<String> toneComboBox = new ComboBox<>();
        toneComboBox.getItems().addAll("Formel", "Amical", "Direct");
        toneComboBox.setValue("Formel");

        Button generateButton = new Button("Générer l'email");
        Button clearButton = new Button("Effacer");
        Button copyButton = new Button("Copier");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setWrapText(true);
        resultArea.setPromptText("L'email généré apparaîtra ici...");

        // Action du bouton Générer
        generateButton.setOnAction(event -> {
            String context = contextField.getText().trim();
            String recipient = recipientField.getText().trim();
            String subject = subjectField.getText().trim();
            String tone = toneComboBox.getValue();

            if (context.isEmpty()) {
                resultArea.setText("Erreur : Veuillez entrer un contexte.");
                return;
            }

            generateButton.setDisable(true);
            clearButton.setDisable(true);
            copyButton.setDisable(true);
            resultArea.setText("Génération en cours...");

            // Tâche asynchrone pour appeler l’API
            Task<String> task = new Task<>() {
                @Override
                protected String call() {
                    try {
                        return openAiHandler.generateEmail(context, recipient, subject, tone);
                    } catch (Exception e) {
                        String errorMsg = e.getMessage();
                        if (errorMsg.contains("401")) {
                            return "Erreur : Clé API invalide ou non autorisée.";
                        } else if (errorMsg.contains("429")) {
                            return "Erreur : Quota de l'API dépassé. Veuillez réessayer plus tard.";
                        } else {
                            return "Erreur : " + errorMsg;
                        }
                    }
                }
            };

            // Gestion des résultats
            task.setOnSucceeded(e -> {
                resultArea.setText(task.getValue());
                generateButton.setDisable(false);
                clearButton.setDisable(false);
                copyButton.setDisable(false);
            });
            task.setOnFailed(e -> {
                resultArea.setText("Erreur lors de la génération : " + task.getException().getMessage());
                generateButton.setDisable(false);
                clearButton.setDisable(false);
                copyButton.setDisable(false);
            });

            new Thread(task).start();
        });

        // Action du bouton Effacer
        clearButton.setOnAction(event -> {
            contextField.clear();
            recipientField.clear();
            subjectField.clear();
            toneComboBox.setValue("Formel");
            resultArea.clear();
        });

        // Action du bouton Copier
        copyButton.setOnAction(event -> {
            String textToCopy = resultArea.getText().trim();
            if (textToCopy.isEmpty() || textToCopy.startsWith("Erreur") || textToCopy.equals("Génération en cours...")) {
                resultArea.setText("Rien à copier. Veuillez générer un email d'abord.");
                return;
            }

            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(textToCopy);
            clipboard.setContent(content);

            // Afficher une confirmation temporaire
            String originalText = resultArea.getText();
            resultArea.setText("Email copié dans le presse-papiers !");
            resultArea.setDisable(true);
            new Thread(() -> {
                try {
                    Thread.sleep(2000); // Afficher le message pendant 2 secondes
                    javafx.application.Platform.runLater(() -> {
                        resultArea.setText(originalText);
                        resultArea.setDisable(false);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        // Mise en page
        HBox buttonBox = new HBox(10, generateButton, clearButton, copyButton);
        VBox root = new VBox(10, contextLabel, contextField, recipientLabel, recipientField,
                subjectLabel, subjectField, toneLabel, toneComboBox, buttonBox, resultArea);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 600, 500);

        // Charger le CSS
        String cssPath = "style.css";
        if (getClass().getResource(cssPath) != null) {
            scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        } else {
            System.err.println("Warning: styles.css not found at " + cssPath);
        }

        // Configuration de la fenêtre
        primaryStage.setTitle("Générateur d'emails professionnels");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}