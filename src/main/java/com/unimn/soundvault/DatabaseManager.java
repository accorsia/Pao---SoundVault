package com.unimn.soundvault;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        executeSqlScript("GoldPlatUpdater.sql");
        System.out.println(Utilities.debHelp() + "> Updating database OK");
    }

    public void executeSqlScript(String scriptFileName)
    {
        String scriptAbsPath = System.getProperty("user.dir") + "\\src\\script\\" + scriptFileName;

        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get(scriptAbsPath).toFile())))
        {
            StringBuilder sb = new StringBuilder();
            String readLine;

            while((readLine = reader.readLine()) != null)
                sb.append(readLine);

            statement.executeUpdate(sb.toString());
            System.out.println(Utilities.debHelp() + "> " + scriptFileName + " successfully executed");
        }
        catch (IOException e)
        {
            System.out.println(Utilities.debHelp() + "ERROR:\tscript absolute path not found:\t" + scriptAbsPath);
            e.printStackTrace();
        }
        catch (SQLException s)
        {
            System.out.println(Utilities.debHelp() + "ERROR:\terror in the script's query");
            s.printStackTrace();
        }
        //  reader automatically closed by the 'try-with-resources' structure

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

    public String getMetadata() throws SQLException {
        DatabaseMetaData dbMd = conn.getMetaData();
        StringBuilder sb = new StringBuilder();
        
        //  db. info
        sb.append("\n---\t Database metadata\t---\n");

        sb.append("Name: " + dbMd.getDatabaseProductName() + "\n");
        sb.append("Version: " + dbMd.getDatabaseProductVersion() + "\n");
        sb.append("Driver (JDBC): " + dbMd.getDriverName() + "\n\n");


        //  Tables info
        sb.append("---\t Tables metadata\t---\n");

        ResultSet tables = dbMd.getTables(null, null, null, null);

        //  Scroll tables
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");

            //  Exclude system tables
            if (!tableName.startsWith("sqlite_"))
            {
                sb.append(tableName + ":\t(");

                //  Scroll columns for each table
                ResultSet columnsResultSet = dbMd.getColumns(null, null, tableName, null);
                while (columnsResultSet.next()) {
                    String columnName = columnsResultSet.getString("COLUMN_NAME");
                    sb.append(columnName + ", ");
                }

                sb.append(")\n");
            }

        }

        return sb.toString();
    }

}
