package castle.comp3021.assignment.protocol.io;


import castle.comp3021.assignment.gui.FXJesonMor;
import javafx.stage.FileChooser;
import java.io.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * This class exports the entire game configuration and procedure to file
 * You need to overwrite .toString method for the class that will be serialized
 * Hint:
 *      - The output folder should be selected in a popup window {@link javafx.stage.FileChooser}
 *      - Read file with {@link java.io.BufferedWriter}
 */
public class Serializer {
    @NotNull
    private static final Serializer INSTANCE = new Serializer();

    /**
     * @return Singleton instance of this class.
     */
    @NotNull
    public static Serializer getInstance() {
        return INSTANCE;
    }


    /**
     * Save a {@link castle.comp3021.assignment.textversion.JesonMor} to file.
     * @param fxJesonMor a fxJesonMor instance under export
     * @throws IOException if an I/O exception has occurred.
     */
    public void saveToFile(FXJesonMor fxJesonMor) throws IOException {
        //TODO
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Save as");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Documents (*.txt)", "*.txt")
        );

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(fxJesonMor.toString());
            output.append("\nEND");
            output.close();
        }
    }

}
