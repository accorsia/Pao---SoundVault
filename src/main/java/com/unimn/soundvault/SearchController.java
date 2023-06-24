package com.unimn.soundvault;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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
    }
}
