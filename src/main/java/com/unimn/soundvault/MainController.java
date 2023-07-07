package com.unimn.soundvault;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

public class MainController {
    @FXML   //  load 'Main.fxml'

    //  (main) AnchorPane
    public AnchorPane mainPane;

    //  TableText area
    public TableView mainTable;

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

    //  TextField
    public TextField artistSearchField;
    public TextField albumSearchField;

    //  Grid pane
    public GridPane artistMetadataPane;
    public GridPane albumMetadataPane;



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //  Populate: 'mainTable'
    public static void populateTable(TableView tableview, ResultSet rs) throws SQLException {

        tableview.getColumns().clear(); //  clean table

        //  Add data to ObservableList<ObservableList> ~ matrix
        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++)
        {
            //  We are using non property style for making dynamic table
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

            tableview.getColumns().addAll(col);
        }

        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        //  Add columns to value
        while(rs.next())
        {
            ObservableList<String> row = FXCollections.observableArrayList();

            //  iterate Row
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++)
                row.add(rs.getString(i));   //  iterate Column

            data.add(row);
        }

        tableview.setItems(data);   //  Add matrix to tableview

    }

    //  Populate 'artist\albumMetadata'
    public static void populateGridPane(GridPane gridPane, ResultSet rs) throws SQLException {

        gridPane.getChildren().clear();     //  remove old label --> so they won't overlap
        ResultSetMetaData md = rs.getMetaData();
        Label name = null;
        Label value = null;

        for(int i=1; i<=md.getColumnCount(); i++)
        {
            name = new Label(md.getColumnName(i));
            value = new Label(rs.getString(i));

            name.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));   //  set style for label

            gridPane.add(name, 0, i-1); //  <column, row>
            gridPane.add(value, 1, i-1);
        }

        gridPane.setGridLinesVisible(true);     //  restore grid lines
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void SearchForArtist(ActionEvent actionEvent) throws SQLException {
        String targetColumn = "Name";       //  default option: 'Name'

        //  Check: something in the search field
        if (artistSearchField.getText().isEmpty())
        {
            String errorMessage = "Scrivi un testo di ricerca";
            CreateAlert(errorMessage);  //  Alert popup
            System.out.println(Utilities.debHelp() + "ERROR:\t" + errorMessage);
        }

        //  Check: one radioButton selected
        else if (artistRadio.getSelectedToggle() == null)
        {
            String errorMessage = "ERROR:\tSeleziona 1 parametro di ricerca";
            CreateAlert(errorMessage);
            System.out.println(Utilities.debHelp() + "ERROR:\t" + errorMessage);
        }

        //  Execute the research
        else
        {
            if (artRadBut_ida.isSelected())
                targetColumn = "Ida";
            else if (artRadBut_stage.isSelected())
                targetColumn = "Stage Name";
            else if (artRadBut_birth.isSelected())
                targetColumn = "Birth";
            else if (artRadBut_name.isSelected())
                targetColumn = "Name";

            String query = "SELECT * FROM Artist WHERE \"" + targetColumn + "\" = \"" + artistSearchField.getText() + "\"";

            populateTable(mainTable, Main.db.executeQuery(query));    //  Update 'mainTable'
            populateGridPane(artistMetadataPane, Main.db.executeQuery(query));
        }


    }

    public void SearchForAlbum(ActionEvent actionEvent) throws SQLException {
        String targetColumn = "Name";       //  default option: 'Name'

        //  Check: something in the search field
        if (albumSearchField.getText().isEmpty())
        {
            String errorMessage = "Scrivi un testo di ricerca";
            CreateAlert(errorMessage);  //  Alert popup
            System.out.println(Utilities.debHelp() + "ERROR:\t" + errorMessage);
        }

        //  Check: one radioButton selected
        else if (albumRadio.getSelectedToggle() == null)
        {
            String errorMessage = "Seleziona 1 parametro di ricerca";
            CreateAlert(errorMessage);  //  Alert popup
            System.out.println(Utilities.debHelp() + "ERROR:\t" + errorMessage);
        }

        //  Execute research
        else
        {
            if (albRadButt_idb.isSelected())
                targetColumn = "idb";
            else if (albRadButt_gold.isSelected())
                targetColumn = "Gold";
            else if (albRadButt_name.isSelected())
                targetColumn = "Name";
            else if (albRadButt_plat.isSelected())
                targetColumn = "Plat";

            String query = "SELECT * FROM Album WHERE \"" + targetColumn + "\" = \"" + albumSearchField.getText() + "\"";

            populateTable(mainTable, Main.db.executeQuery(query));
            populateGridPane(albumMetadataPane, Main.db.executeQuery(query));

            //  Also show metadata for the artist's album
            String albumIda = Main.db.executeQuery(query).getString(6);
            String artistQuery = "SELECT * FROM Artist where \"Ida\" = \"" +  albumIda + "\"";
            populateGridPane(artistMetadataPane, Main.db.executeQuery(artistQuery));
        }


    }

    private void CreateAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Error during input");
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    //  Debugger sake
    public static void GetArtistMetadata(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        StringBuilder sb = new StringBuilder();

        for(int i=1; i<=md.getColumnCount(); i++)
            sb.append(md.getColumnName(i) + ":\t" + rs.getString(i) + "\n");

        System.out.println(Utilities.debHelp() + sb);
    }


    public void OpenAbout(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);

        //  Create about info text
        String sb = "SoundVault by Alessandro Accorsi\n" +  //  author metadata
                "github: https://github.com/AlessandroVerdad/Pao--SoundVault\n" +
                DatabaseSafeGetter.db.getMetadata();    //  database metadata

        alert.setContentText(sb);
        alert.showAndWait();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void CreateAddArtistPane(ActionEvent event) throws IOException {  //  IOException --> function could not find .fxml file

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddArtist.fxml"));   //  .fxml
        Parent addArtistRoot = loader.load();                               //  root
        AddArtistController addArtistController = loader.getController();   //  controller

        /* Se invece volessi aggiungere il pannello al contenitore principale
        mainPane.getChildren().add(addArtistRoot);
         */

        //  Create stage + scene for the new panel
        Scene addArtistScene = new Scene(addArtistRoot);
        Stage addArtistStage = new Stage();
        addArtistStage.setScene(addArtistScene);

        //  Icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("img/artist.png")).openStream());
        addArtistStage.getIcons().add(icon);

        addArtistStage.setTitle("Artist");
        addArtistStage.show();
    }

    public void CreateAddAlbumPane(ActionEvent event) throws IOException {  //  IOException --> function could not find .fxml file

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAlbum.fxml"));   //  .fxml
        Parent addAlbumRoot = loader.load();                               //  root
        AddAlbumController addAlbumController = loader.getController();   //  controller

        /* Se invece volessi aggiungere il pannello al contenitore principale
        mainPane.getChildren().add(addAlbumRoot);
         */

        //  Create stage + scene for the new panel
        Scene addAlbumScene = new Scene(addAlbumRoot);
        Stage addAlbumStage = new Stage();
        addAlbumStage.setScene(addAlbumScene);

        //  Icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("img/album.png")).openStream());
        addAlbumStage.getIcons().add(icon);

        addAlbumStage.setTitle("Album");
        addAlbumStage.show();
    }


}
