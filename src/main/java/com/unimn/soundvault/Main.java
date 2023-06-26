package com.unimn.soundvault;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

    static DatabaseManager db;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        //  Safely connect to database
        db = DatabaseSafeGetter.getDb();
        System.out.println(Utilities.debHelp() + "> Main.java got the database!");

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //  Connect .fxml
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
        //  ...OPPURE...
        //  FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        //  Scene scene = new Scene(fxmlLoader.load(), 320, 240);*/


        //  Set containers
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Image icon = new Image(Objects.requireNonNull(getClass().getResource("img/icon.png")).openStream());
        stage.getIcons().add(icon);

        stage.setTitle("SoundVault!");
        stage.show();


        //  Close database when you close the form
        stage.setOnCloseRequest(event -> {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}