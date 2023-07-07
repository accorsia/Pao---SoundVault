package com.unimn.soundvault;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddAlbumController implements Initializable {
    @FXML   //  load 'AddAlbum.fxml'

    public ChoiceBox<String> idaChoiceBox;

    public TextField nameTxFld;
    public TextField releaseTxFld;

    public RadioButton goldRadBtn;
    public RadioButton platRadBtn;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //  Convert single columns to ArrayList
    public ArrayList<String> Column2Array(String column, String table) {
        ArrayList<String> columnsList = new ArrayList<>();

        try {
            ResultSet rs = Main.getDb().executeQuery("select \"" + column + "\" from " + table + ";");

            while (rs.next())
                columnsList.add(rs.getString(1));

        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tConverting column to array");
            e.printStackTrace();
        }

        return columnsList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    Initialize a controller once his root element has already been processed
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> idaList = Column2Array("ida", "Artist");
        ArrayList<String> nameList = Column2Array("Stage Name", "Artist");

        ArrayList<String> itemCheckBox = new ArrayList<>();

        for(int i=0; i<idaList.size(); i++)
            itemCheckBox.add(i, (idaList.get(i) + " - " + nameList.get(i)));

        idaChoiceBox.getItems().addAll(itemCheckBox);
    }

    public void SaveButton(ActionEvent actionEvent) {
        int ida = Integer.parseInt(idaChoiceBox.getValue().split("\\s*-\\s*")[0].trim());
        String name = nameTxFld.getText();
        String release = releaseTxFld.getText();
        boolean gold = goldRadBtn.isSelected();
        boolean plat = platRadBtn.isSelected();

        Main.getDb().addAlbumRecord(name, release, gold, plat, ida);

        System.out.println(Utilities.debHelp() + "Query executed! Album added!");
    }
}
