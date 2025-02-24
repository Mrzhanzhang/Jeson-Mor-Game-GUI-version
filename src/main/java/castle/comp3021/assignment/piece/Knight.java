package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;
import castle.comp3021.assignment.gui.controllers.Renderer;
import castle.comp3021.assignment.gui.controllers.ResourceLoader;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Knight piece that moves similar to knight in chess.
 * Rules of move of Knight can be found in wikipedia (https://en.wikipedia.org/wiki/Knight_(chess)).
 *
 * @see <a href='https://en.wikipedia.org/wiki/Knight_(chess)'>Wikipedia</a>
 */
public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'K';
    }

    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        var moves = new ArrayList<Move>();
        var steps = new int[]{1, -1, 2, -2};
        for (var stepX : steps) {
            for (var stepY : steps) {
                var destination = new Place(source.x() + stepX, source.y() + stepY);
                if (Math.abs(destination.x() - source.x()) + Math.abs(destination.y() - source.y()) == 3) {
                    moves.add(new Move(source, destination));
                }
            }
        }
        return moves.stream().filter(move -> validateMove(game, move)).toArray(Move[]::new);
    }

    private boolean validateMove(Game game, Move move) {
        var rules = new Rule[] {
                new OutOfBoundaryRule(),
                new OccupiedRule(),
                new VacantRule(),
                new BelongingRule(this.getPlayer()),
                new NilMoveRule(),
                new FirstNMovesProtectionRule(game.getConfiguration().getNumMovesProtection()),
                new KnightMoveRule(),
                new KnightBlockRule(),
        };
        for (var rule : rules) {
            if (!rule.validate(game, move)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Render archer pieces to the corresponding images implemented in {@link ResourceLoader getImage}
     * Hint: consider different color of knight pieces.
     * Throw IllegalStateException exception if failed to load image.
     * @return {@link Renderer.CellImage}
     */
    public Renderer.@NotNull CellImage getImageRep() {
        // TODO
        Image whiteKTile = ResourceLoader.getImage('K');
        Image blackKTile = ResourceLoader.getImage('k');
        if (whiteKTile.isError() || blackKTile.isError()) {
            throw new IllegalStateException("Failed to load image");
        }

        if (this.getPlayer().getName().equals("White")) {
            return new Renderer.CellImage(whiteKTile);
        } else {
            return new Renderer.CellImage(blackKTile);
        }
    }
}
