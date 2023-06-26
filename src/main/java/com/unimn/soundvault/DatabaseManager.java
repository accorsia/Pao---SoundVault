package com.unimn.soundvault;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {

    protected Connection conn;
    protected Statement statement;

    public DatabaseManager() throws SQLException, IOException {
        /*
        1)  Load vendor specific class

        Class.forName("org.sqlite.jdbc");
         */

        System.out.print(Utilities.debHelp() + "> Connecting to database...");  //  debug

        conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");    //  2)  Establish a connection
        statement = conn.createStatement();     //  3)  Create JDBC Statement

        System.out.println("OK!");  //  debug
    }

    public void close() throws SQLException {
        if (conn != null) {
            statement.close();
            conn.close();
        }

        System.out.print(Utilities.debHelp() + "> Closing database...\n");  //  debug
    }

    public void updateDb() throws SQLException, IOException {
        System.out.print(Utilities.debHelp() + "> Updating database...");
        executeSqlScript("GoldPlatUpdater.sql");
        System.out.println("...OK");
    }

    public void executeSqlScript(String scriptFileName) throws IOException, SQLException
    {
        String scriptAbsPath = System.getProperty("user.dir") + "\\src\\SQLScripts\\" + scriptFileName;
        Path scriptPath = Paths.get(scriptAbsPath);

        BufferedReader reader = new BufferedReader(new FileReader(scriptPath.toFile()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        statement.executeUpdate(sb.toString());
    }

    public void restore() throws SQLException, IOException {
        statement.executeQuery("drop table if exists Artist");  //  delete
        executeSqlScript("CreateDb.sql");               //  create
        executeSqlScript("RestoreArtistValues.sql");    //  fill Artist
        executeSqlScript("RestoreAlbumValues.sql");     //  fill Album
    }

    //  Read
    public ResultSet executeQuery(String query) throws SQLException {
        System.out.print(Utilities.debHelp() + "> Executing the query:\t\"" + query + "\"\n");
        return statement.executeQuery(query);
    }

    //  Write
    public int executeUpdate(String query) throws SQLException {

        System.out.print(Utilities.debHelp() + "> Updating database...\n");
        return statement.executeUpdate(query);
    }

    public String printRs(ResultSet rs) throws SQLException {
        return Utilities.printRs(rs);
    }

    public void showMetadata() throws SQLException {
        DatabaseMetaData dbMd = conn.getMetaData();

        //  db. info
        System.out.println("---\t Database metadata\t---");

        System.out.println("Name: " + dbMd.getDatabaseProductName());
        System.out.println("Version: " + dbMd.getDatabaseProductVersion());
        System.out.println("Driver (JDBC): " + dbMd.getDriverName());
        System.out.println();


        //  Tables info
        System.out.println("---\t Tables metadata\t---");

        ResultSet tableResultSet = dbMd.getTables(null, null, null, null);
        while (tableResultSet.next()) {
            String tableName = tableResultSet.getString("TABLE_NAME");
            System.out.println("Tabella: " + tableName);
        }
        tableResultSet.close();
    }

}
