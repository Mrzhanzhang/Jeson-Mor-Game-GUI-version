package castle.comp3021.assignment.gui.views.panes;

import castle.comp3021.assignment.gui.FXJesonMor;
import castle.comp3021.assignment.gui.ViewConfig;
import castle.comp3021.assignment.gui.controllers.SceneManager;
import castle.comp3021.assignment.gui.views.BigButton;
import castle.comp3021.assignment.gui.views.BigVBox;
import castle.comp3021.assignment.gui.views.NumberTextField;
import castle.comp3021.assignment.player.ConsolePlayer;
import castle.comp3021.assignment.player.RandomPlayer;
import castle.comp3021.assignment.protocol.Configuration;
import castle.comp3021.assignment.protocol.Player;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GamePane extends BasePane {
    @NotNull
    private final VBox container = new BigVBox();
    @NotNull
    private final Label title = new Label("Jeson Mor");
    @NotNull
    private final Button playButton = new BigButton("Play");
    @NotNull
    private final Button returnButton = new BigButton("Return");
    @NotNull
    private final Button useDefaultButton = new BigButton("Use Default");
    @NotNull
    private final Button isHumanPlayer1Button = new BigButton("");
    @NotNull
    private final Button isHumanPlayer2Button = new BigButton("");

    @NotNull
    private final NumberTextField sizeFiled = new NumberTextField("");


    private final BorderPane sizeBox = new BorderPane(null, null, sizeFiled, null, new Label("Size of Board:"));

    @NotNull
    private final NumberTextField numMovesProtectionField = new NumberTextField("");

    @NotNull
    private final BorderPane numMovesProtectionBox = new BorderPane(null, null,
            numMovesProtectionField, null, new Label("Protection Moves:"));


    private FXJesonMor fxJesonMor = null;

    public GamePane() {
        fillValues();
        connectComponents();
        styleComponents();
        setCallbacks();
    }

    /**
     * Add components to corresponding containers
     * Connects all components into the {@link BorderPane}.
     */
    @Override
    void connectComponents() {
        //TODO
        container.getChildren().addAll(title, sizeBox, numMovesProtectionBox,
                isHumanPlayer1Button, isHumanPlayer2Button, useDefaultButton, playButton, returnButton);
        setCenter(container);
    }

    @Override
    void styleComponents() {
        title.getStyleClass().add("head-size");
    }

    /**
     * Set callbacks to buttons
     * Hint:
     * -  When fill in board size and step of protections, numbers need to validate
     * -  useDefaultButton: use default value for {@link GamePane#sizeFiled}, {@link GamePane#numMovesProtectionField}, and two players
     *    as they are saved in {@link SettingPane}
     * -  The current configuration (including {@link GamePane#sizeFiled}, {@link GamePane#numMovesProtectionField} and two players role)
     *    should not affect the default settings.
     * -  After clicking "play" button, the handler is implemented in {@link GamePane#startGame(FXJesonMor)},
     *    which links to {@link GamePlayPane} using the current configuration.
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

        useDefaultButton.setOnAction(mouseEvent -> fillValues());

        playButton.setOnAction(mouseEvent -> {
            boolean validSize = false;
            try {
                int size = sizeFiled.getValue();
                validSize = true;
                int num = numMovesProtectionField.getValue();
                var validationMsg = validate(size, num);
                if (validationMsg.isEmpty()) {  // no warning, can start the game
                    Player player1 = null;
                    Player player2 = null;
                    if (isHumanPlayer1Button.getText().contains("Computer")) {
                        player1 = new RandomPlayer("White");
                    } else {
                        player1 = new ConsolePlayer("White");
                    }

                    if (isHumanPlayer2Button.getText().contains("Human")) {
                        player2 = new ConsolePlayer("Black");
                    } else {
                        player2 = new RandomPlayer("Black");
                    }

                    Player[] players = new Player[]{player1, player2};
                    fxJesonMor = new FXJesonMor(new Configuration(size, players, num));
                    startGame(fxJesonMor);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Validation failed");
                    alert.setContentText(validationMsg.get());
                    alert.showAndWait();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Validation failed");
                if (validSize) {
                    alert.setContentText("Incorrect format of Protection Moves");
                } else {
                    alert.setContentText("Incorrect format of Size of Board");
                }
                alert.showAndWait();
            }
        });

        returnButton.setOnAction(mouseEvent -> {
            fillValues();  // set to default before returning
            SceneManager.getInstance().showPane(MainMenuPane.class);
        });
    }

    /**
     * Handler when clicking "play" button, using the current configuration to pass a {@link FXJesonMor} instance
     * Hint:
     *      - You may need to initialize and set up {@link GamePlayPane} by passing {@link FXJesonMor}
     * @param fxJesonMor an instance of {@link FXJesonMor}
     */
    void startGame(@NotNull FXJesonMor fxJesonMor) {
        final var gameplayPane = SceneManager.getInstance().<GamePlayPane>getPane(GamePlayPane.class);
        gameplayPane.initializeGame(fxJesonMor);
        SceneManager.getInstance().showPane(GamePlayPane.class);
    }

    /**
     * Fill in the default values for all editable fields.
     */
    void fillValues() {
        // TODO
        isHumanPlayer1Button.setText("Player 1: " + (globalConfiguration.isFirstPlayerHuman() ? "Human" : "Computer"));
        isHumanPlayer2Button.setText("Player 2: " + (globalConfiguration.isSecondPlayerHuman() ? "Human" : "Computer"));
        sizeFiled.setText(String.valueOf(globalConfiguration.getSize()));
        numMovesProtectionField.setText(String.valueOf(globalConfiguration.getNumMovesProtection()));
    }

    /**
     * Validate the text fields
     * The useful msg are predefined in {@link ViewConfig#MSG_BAD_SIZE_NUM}, etc.
     * @param size number in {@link GamePane#sizeFiled}
     * @param numProtection number in {@link GamePane#numMovesProtectionField}
     * @return If validation failed, {@link Optional} containing the reason message;
     * An empty {@link Optional} otherwise.
     */
    public static Optional<String> validate(int size, int numProtection) {
        //TODO
        if (size < 3) {
            return Optional.of(ViewConfig.MSG_BAD_SIZE_NUM);
        }
        if (size % 2 == 0) {
            return Optional.of(ViewConfig.MSG_ODD_SIZE_NUM);
        }
        if (size > 26) {
            return Optional.of(ViewConfig.MSG_UPPERBOUND_SIZE_NUM);
        }
        if (numProtection < 0) {
            return Optional.of(ViewConfig.MSG_NEG_PROT);
        }
        return Optional.empty();
    }

}
