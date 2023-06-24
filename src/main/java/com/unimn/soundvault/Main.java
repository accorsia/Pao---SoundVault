package com.unimn.soundvault;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

    DatabaseManager db;

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        //  Safely connect to database
        this.db = DatabaseSafeGetter.main();
        System.out.println(Utilities.debHelp() + "> Main.java got the database!");

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*  Connect .fxml   */

        //  Opzione 1
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));

        //  Opzione 2
        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);*/


        /*  Set containers  */
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();









        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        stage.setTitle("SoundVault!");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}