package com.unimn.soundvault;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAlbumController implements Initializable {
    public ChoiceBox<String> idaChoiceBox;

    @FXML   //  load 'AddAlbum.fxml'

    //  Convert single columns to ArrayList
    public ArrayList<String> Column2Array(String column, String table) {
        ArrayList<String> columnsList = new ArrayList<>();

        try {
            ResultSet rs = Main.getDb().executeQuery("select " + column + " from " + table + ";");

            while (rs.next())
                columnsList.add(rs.getString(1));

        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tConverting column to array");
            e.printStackTrace();
        }

        return columnsList;
    }

    /*
    Initialize a controller once his root element has already been processed
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idaChoiceBox.getItems().addAll(Column2Array("ida", "Artist"));
    }
}
