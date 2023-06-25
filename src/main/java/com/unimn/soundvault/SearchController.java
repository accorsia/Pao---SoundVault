package com.unimn.soundvault;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SearchController {
    @FXML   //  load 'Main.fxml'

    //  Text area
    public TextArea tableShower;

    //  Radio button
    public ToggleGroup artistRadio;

    public RadioButton artRadBut_stage;
    public RadioButton artRadBut_birth;
    public RadioButton artRadBut_name;
    public RadioButton artRadBut_ida;

    public ToggleGroup albumRadio;

    public RadioButton albRadButt_idb;
    public RadioButton albRadButt_gold;
    public RadioButton albRadButt_name;
    public RadioButton albRadButt_plat;

    public TextField artistSearchField;
    public TextField albumSearchField;
    public TitledPane artistMetadata;
    public TitledPane albumMetadata;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void SearchForArtist(ActionEvent actionEvent) throws SQLException {
        String targetColumn = "Name";       //  default option: 'Name'

        //  TODO:   Could implement a listener to avoid this switch ---> Currently, more than 1 radioButton can be selected
        //   at the same time
        if (artRadBut_ida.isSelected())
            targetColumn = "Ida";
        else if (artRadBut_stage.isSelected())
            targetColumn = "Stage Name";
        else if (artRadBut_birth.isSelected())
            targetColumn = "Birth";
        else if (artRadBut_name.isSelected())
            targetColumn = "Name";

        String query = "SELECT * FROM Artist WHERE \"" + targetColumn + "\" = \"" + artistSearchField.getText() + "\"";
        System.out.println(Utilities.debHelp() + "Query:\t" + query);
        String tableToShow = Utilities.printRs(Main.db.executeQuery(query));

        tableShower.setText(tableToShow);
        GetArtistMetadata(Main.db.executeQuery(query));    //  launch 'artistMetadata' update
    }

    public void SearchForAlbum(ActionEvent actionEvent) throws SQLException {
        String targetColumn = "Name";       //  default option: 'Name'

        //  TODO:   Could implement a listener to avoid this switch ---> Currently, more than 1 radioButton can be selected
        //   at the same time
        if (albRadButt_idb.isSelected())
            targetColumn = "idb";
        else if (albRadButt_gold.isSelected())
            targetColumn = "Name";
        else if (albRadButt_name.isSelected())
            targetColumn = "Release";
        else if (albRadButt_plat.isSelected())
            targetColumn = "Gold";

        String query = "SELECT * FROM Album WHERE \"" + targetColumn + "\" = \"" + albumSearchField.getText() + "\"";
        System.out.println(Utilities.debHelp() + "Query:\t" + query);
        String tableToShow = Utilities.printRs(Main.db.executeQuery(query));

        tableShower.setText(tableToShow);
    }

    //  TODO:    metadata shower - event handler
    public void GetArtistMetadata(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        StringBuilder sb = new StringBuilder();

        for(int i=1; i<=md.getColumnCount(); i++)
            sb.append(md.getColumnName(i) + ":\t" + rs.getString(i) + "\n");

        artistMetadata.setAccessibleText(sb.toString());
        System.out.println(Utilities.debHelp() + sb.toString());

    }

}
