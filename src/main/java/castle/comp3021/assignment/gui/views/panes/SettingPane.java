package castle.comp3021.assignment.gui.views.panes;

import castle.comp3021.assignment.gui.DurationTimer;
import castle.comp3021.assignment.gui.ViewConfig;
import castle.comp3021.assignment.gui.controllers.AudioManager;
import castle.comp3021.assignment.gui.controllers.SceneManager;
import castle.comp3021.assignment.gui.views.BigButton;
import castle.comp3021.assignment.gui.views.BigVBox;
import castle.comp3021.assignment.gui.views.NumberTextField;
import castle.comp3021.assignment.gui.views.SideMenuVBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SettingPane extends BasePane {
    @NotNull
    private final Label title = new Label("Jeson Mor <Game Setting>");
    @NotNull
    private final Button saveButton = new BigButton("Save");
    @NotNull
    private final Button returnButton = new BigButton("Return");
    @NotNull
    private final Button isHumanPlayer1Button = new BigButton("Player 1: ");
    @NotNull
    private final Button isHumanPlayer2Button = new BigButton("Player 2: ");
    @NotNull
    private final Button toggleSoundButton = new BigButton("Sound FX: Enabled");

    @NotNull
    private final VBox leftContainer = new SideMenuVBox();

    @NotNull
    private final NumberTextField sizeFiled = new NumberTextField(String.valueOf(globalConfiguration.getSize()));

    @NotNull
    private final BorderPane sizeBox = new BorderPane(null, null, sizeFiled, null, new Label("Board size"));

    @NotNull
    private final NumberTextField durationField = new NumberTextField(String.valueOf(DurationTimer.getDefaultEachRound()));
    @NotNull
    private final BorderPane durationBox = new BorderPane(null, null, durationField, null,
            new Label("Max Duration (s)"));

    @NotNull
    private final NumberTextField numMovesProtectionField =
            new NumberTextField(String.valueOf(globalConfiguration.getNumMovesProtection()));
    @NotNull
    private final BorderPane numMovesProtectionBox = new BorderPane(null, null,
            numMovesProtectionField, null, new Label("Steps of protection"));

    @NotNull
    private final VBox centerContainer = new BigVBox();
    @NotNull
    private final TextArea infoText = new TextArea(ViewConfig.getAboutText());


    public SettingPane() {
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * Add components to corresponding containers
     */
    @Override
    void connectComponents() {
        //TODO
        leftContainer.getChildren().addAll(title, sizeBox, numMovesProtectionBox, durationBox,
                isHumanPlayer1Button, isHumanPlayer2Button, toggleSoundButton, saveButton, returnButton);
        centerContainer.getChildren().add(infoText);
        setLeft(leftContainer);
        setCenter(centerContainer);
        fillValues();
    }

    @Override
    void styleComponents() {
        infoText.getStyleClass().add("text-area");
        infoText.setEditable(false);
        infoText.setWrapText(true);
        infoText.setPrefHeight(ViewConfig.HEIGHT);
    }

    /**
     * Add handlers to buttons, textFields.
     * Hint:
     *  - Text of {@link SettingPane#isHumanPlayer1Button}, {@link SettingPane#isHumanPlayer2Button},
     *            {@link SettingPane#toggleSoundButton} should be changed accordingly
     *  - You may use:
     *      - Configuration#isFirstPlayerHuman()},
     *      - Configuration#isSecondPlayerHuman()},
     *      - Configuration#setFirstPlayerHuman(boolean)}
     *      - Configuration#setSecondPlayerHuman(boolean)},
     *      - AudioManager#setEnabled(boolean)},
     *      - AudioManager#isEnabled()},
     */
    @Override
    void setCallbacks() {
        //TODO
        isHumanPlayer1Button.setOnAction(mouseEvent -> {
            isHumanPlayer1Button.setText("Player 1: " + (isHumanPlayer1Button.getText().contains("Computer")? "Human" : "Computer"));
        });

        isHumanPlayer2Button.setOnAction(mouseEvent -> {
            isHumanPlayer2Button.setText("Player 2: " + (isHumanPlayer2Button.getText().contains("Computer")? "Human" : "Computer"));
        });

        toggleSoundButton.setOnAction(mouseEvent -> {
            toggleSoundButton.setText("Sound FX: " + (toggleSoundButton.getText().contains("Enabled") ? "Disabled" : "Enabled"));
        });

        saveButton.setOnAction(mouseEvent -> {
            boolean validSize = false;
            boolean validNum = false;
            try {
                int size = sizeFiled.getValue();
                validSize = true;
                int num = numMovesProtectionField.getValue();
                validNum = true;
                int duration = durationField.getValue();
                var validationMsg = validate(size, num, duration);

                if (validationMsg.isEmpty()) {  // no warning, then modify the default values
                    globalConfiguration.setSize(size);
                    globalConfiguration.setNumMovesProtection(num);
                    DurationTimer.setDefaultEachRound(duration);
                    globalConfiguration.setFirstPlayerHuman(isHumanPlayer1Button.getText().contains("Human"));
                    globalConfiguration.setSecondPlayerHuman(isHumanPlayer2Button.getText().contains("Human"));
                    AudioManager.getInstance().setEnabled(toggleSoundButton.getText().contains("Enable"));
                    returnToMainMenu(true);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Validation failed");
                    alert.setContentText(validationMsg.get());
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Validation failed");
                if (validSize && !validNum) {
                    alert.setContentText("Incorrect format of Protection Moves");
                } else if (validNum) {
                    alert.setContentText("Incorrect format of Max Duration");
                } else {
                    alert.setContentText("Incorrect format of Size of Board");
                }
                alert.showAndWait();
            }
        });

        returnButton.setOnAction(mouseEvent -> {
            returnToMainMenu(false);
        });
    }

    /**
     * Fill in the default values for all editable fields.
     */
    private void fillValues() {
        // TODO
        isHumanPlayer1Button.setText("Player 1: " + (globalConfiguration.isFirstPlayerHuman() ? "Human" : "Computer"));
        isHumanPlayer2Button.setText("Player 2: " + (globalConfiguration.isSecondPlayerHuman() ? "Human" : "Computer"));
        toggleSoundButton.setText("Sound FX: " + (AudioManager.getInstance().isEnabled() ? "Enabled" : "Disabled"));
        sizeFiled.setText(String.valueOf(globalConfiguration.getSize()));
        numMovesProtectionField.setText(String.valueOf(globalConfiguration.getNumMovesProtection()));
        durationField.setText(String.valueOf(DurationTimer.getDefaultEachRound()));
    }


    /**
     * Switches back to the {@link MainMenuPane}.
     *
     * @param writeBack Whether to save the values present in the text fields to their respective classes.
     */
    private void returnToMainMenu(final boolean writeBack) {
        //TODO
        if (writeBack) {  // save button clicked
            final var gamePane = SceneManager.getInstance().<GamePane>getPane(GamePane.class);
            gamePane.fillValues(); // update default of gamePane before returning
        }
        fillValues();  // update default of settingPane before returning
        SceneManager.getInstance().showPane(MainMenuPane.class);
    }

    /**
     * Validate the text fields
     * The useful msgs are predefined in {@link ViewConfig#MSG_BAD_SIZE_NUM}, etc.
     * @param size number in {@link SettingPane#sizeFiled}
     * @param numProtection number in {@link SettingPane#numMovesProtectionField}
     * @param duration number in {@link SettingPane#durationField}
     * @return If validation failed, {@link Optional} containing the reason message; An empty {@link Optional}
     *      * otherwise.
     */
    public static Optional<String> validate(int size, int numProtection, int duration) {
        //TODO
        if (size < 3) {
            return Optional.of(ViewConfig.MSG_BAD_SIZE_NUM);
        }
        if (size % 2 != 1) {
            return Optional.of(ViewConfig.MSG_ODD_SIZE_NUM);
        }
        if (size > 26) {
            return Optional.of(ViewConfig.MSG_UPPERBOUND_SIZE_NUM);
        }
        if (numProtection < 0) {
            return Optional.of(ViewConfig.MSG_NEG_PROT);
        }
        if (duration <= 0) {
            return Optional.of(ViewConfig.MSG_NEG_DURATION);
        }
        return Optional.empty();
    }
}
