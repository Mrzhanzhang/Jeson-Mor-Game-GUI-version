package castle.comp3021.assignment.gui;

import castle.comp3021.assignment.gui.controllers.SceneManager;
import castle.comp3021.assignment.gui.views.panes.GamePlayPane;
import castle.comp3021.assignment.textversion.JesonMor;
import castle.comp3021.assignment.protocol.*;
import castle.comp3021.assignment.gui.controllers.Renderer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import org.jetbrains.annotations.NotNull;

public class FXJesonMor extends JesonMor {

    @NotNull
    private DurationTimer durationTimer;

    private final IntegerProperty scorePlayer1Property = new SimpleIntegerProperty(0);

    private final IntegerProperty scorePlayer2Property = new SimpleIntegerProperty(0);

    private final StringProperty currentPlayerNameProperty = new SimpleStringProperty(getCurrentPlayer().getName());

    /**
     * Initialize an instance of {@link JesonMor}
     * Hint:
     *     - Also consider durationTimer
     * @param configuration
     */
    public FXJesonMor(Configuration configuration) {
        //TODO
        super(configuration);
        this.configuration.setAllInitialPieces();
        durationTimer = new DurationTimer();
    }


    /**
     * This method can be used in {@link castle.comp3021.assignment.gui.views.panes.GamePlayPane}
     * Entry of render board and pieces using {@link Renderer#renderChessBoard(Canvas, int, Place)}
     * and {@link Renderer#renderPieces(Canvas, Piece[][])}
     * @param canvas render the given canvas
     */
    public void renderBoard(@NotNull Canvas canvas) {
        //TODO
        Renderer.renderChessBoard(canvas, this.configuration.getSize(), this.configuration.getCentralPlace());
        Renderer.renderPieces(canvas, this.board);
    }



    /**
     * Adds a handler to be run when a tick elapses.
     *
     * @param handler {@link Runnable} to execute.
     */
    public void addOnTickHandler(@NotNull Runnable handler) {
        durationTimer.registerTickCallback(handler);
    }


    /**
     * Starts the timer
     */
    public void startCountdown() {
        durationTimer.start();
    }

    /**
     * Stops the timer
     */
    public void stopCountdown() {
        durationTimer.stop();
    }

    public StringProperty getCurPlayerName() {
        return currentPlayerNameProperty;
    }

    public IntegerProperty getPlayer1Score() {
        return scorePlayer1Property;
    }

    public IntegerProperty getPlayer2Score() {
        return scorePlayer2Property;
    }

    public void updateNumMoves() {
        this.numMoves++;
    }

    /**
     * Update the score of a player according to the piece and corresponding move made by him just now.
     *
     * @param player the player who just makes a move
     * @param piece  the piece that is just moved
     * @param move   the move that is just made
     */
    @Override
    public void updateScore(Player player, Piece piece, Move move) {
        var newScore = 0;
        newScore = Math.abs(move.getSource().x() - move.getDestination().x());
        newScore += Math.abs(move.getSource().y() - move.getDestination().y());
        player.setScore(player.getScore() + newScore);

        // update score to 2 properties
        // TODO: update scorePlayer1Property and scorePlayer2Property
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (player.getName().equals("White")) {
                    scorePlayer1Property.setValue(player.getScore());
                } else {
                    scorePlayer2Property.setValue(player.getScore());
                }
                SceneManager.getInstance().<GamePlayPane>getPane(GamePlayPane.class).getTicksElapsed().setValue(0);  // reset timer countdown
                currentPlayerNameProperty.setValue(getCurrentPlayer().getName());
            }
        });
    }

}