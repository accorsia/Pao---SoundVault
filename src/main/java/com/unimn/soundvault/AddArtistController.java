package com.unimn.soundvault;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Objects;

public class AddArtistController {
    @FXML   //  load 'AddArtist.fxml'

    public TextField stagenameTxFld;
    public TextField nameTxFld;
    public TextField birthTxFld;
    public TextField goldTxFld;
    public TextField platTxFld;

    public Button saveButton;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void SaveButton(ActionEvent event) {
        
        String addQuery = "";
        //  'ida' not necessary --> automatically calculated
        String stagename = stagenameTxFld.getText();
        String name = nameTxFld.getText();
        String birth = birthTxFld.getText();


        //  Select which function to call according to user full or empty input of '# Gold' and '# Plat'
        if (Objects.equals(goldTxFld.getText(), "") && Objects.equals(platTxFld.getText(), ""))
            addQuery = Main.getDb().addArtistRecord(stagename, name, birth);
        else if (!Objects.equals(goldTxFld.getText(), "") && !Objects.equals(platTxFld.getText(), "")) {
            int gold = Integer.parseInt(goldTxFld.getText());
            int plat = Integer.parseInt(platTxFld.getText());

            addQuery = Main.getDb().addArtistRecord(stagename, name, birth, gold, plat);
        }
        else
            System.out.println(Utilities.debHelp() + "ERROR\t: #Gold and #Plat TextField need to be both empty or both full");

        System.out.println(Utilities.debHelp() + "Query executed! Artist added!");


    }

}
