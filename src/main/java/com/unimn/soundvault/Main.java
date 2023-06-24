package com.unimn.soundvault;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends Application {

    DatabaseManager db;
    @Override
    public void start(Stage mainStage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 320, 240);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        this.db = DatabaseSafeGetter.main();
        System.out.println(Utilities.debHelp() + "> Start got database!");

        String query = "SELECT * FROM Artist";
        ResultSet rs = db.executeQuery(query);
        Utilities.printRs(rs);

        /*Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource("SampleXML.fxml")));
        Scene scene = new Scene(root);*/

        // Menu bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Esci");
        fileMenu.getItems().add(exitMenuItem);
        menuBar.getMenus().add(fileMenu);

        // Creazione dei componenti per le tab
        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Artist");
        TextArea tabArea = new TextArea();
        tab1.setContent(tabArea);

        Tab tab2 = new Tab("Album");

        tabPane.getTabs().addAll(tab1, tab2);

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                if (newTab != null) {
                    System.out.println("Tab changed to: " + newTab.getText());
                }
            }
        });

        /*String query = "SELECT * FROM Artist";
        ResultSet rs = db.executeQuery(query);
        tabArea.setText(db.printRs(rs));*/

        // Creazione del frame laterale
        VBox sidePanel = new VBox();
        sidePanel.setPrefWidth(200);
        sidePanel.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);
        borderPane.setLeft(sidePanel);

        Scene scene = new Scene(borderPane, 600, 400);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        mainStage.setTitle("SoundVault!");
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}