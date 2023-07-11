package com.unimn.soundvault;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.text.MessageFormat;

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

    public void close() {
        try
        {
            if (conn != null) {
                statement.close();
                conn.close();
            }
        } catch (SQLException e) {
            System.out.print(Utilities.debHelp() + "ERROR:\t closing database\n");
            e.printStackTrace();
        }

        System.out.print(Utilities.debHelp() + "> Closing database...\n");  //  debug
    }

    public void updateDb() throws IOException {
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

    public void restore() {
        try {
            statement.executeQuery("drop table if exists Artist");  // delete
            executeSqlScript("CreateDb.sql");                       // create
            executeSqlScript("RestoreArtistValues.sql");            // fill Artist
            executeSqlScript("RestoreAlbumValues.sql");             // fill Album
        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tRestoring the database");
            e.printStackTrace();
        }

    }

    //  Read
    public ResultSet executeQuery(String query) {
        try
        {
            System.out.print(Utilities.debHelp() + "> Executing the query:\t\"" + query + "\"\n");
            return statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            System.out.println(Utilities.debHelp() + "ERROR: Query:\t" + query);
            e.printStackTrace();
        }
        return null;
    }

    //  Write
    public int executeUpdate(String query) {

        try
        {
            System.out.print(Utilities.debHelp() + "> Updating database...\n");
            return statement.executeUpdate(query);
        }
        catch (SQLException e)
        {
            System.out.println(Utilities.debHelp() + "ERROR: Query:\t" + query);
            e.printStackTrace();
        }
        return -1;
    }

    //  Overload: 1st version
    public String addArtistRecord(String stageName, String name, String birth)  {
        String addQuery = "insert into Artist (\"Stage Name\", Name, Birth)\n" +
                "values\n" +
                "('" + stageName + "' , '" + name + "' , " + birth + ");" ;

        try
        {
            statement.executeUpdate(addQuery);
        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tAdding an Artist (3 param) --> query: " + addQuery);
            e.printStackTrace();
        }
        return addQuery;
    }

    //  Overload: 2nd version
    public String addArtistRecord(String stageName, String name, String birth, int nGold, int nPlat) {
        String addQuery = "insert into Artist (\"Stage Name\", Name, Birth, \"# Gold\", \"# Plat\")\n" +
                "values\n" +
                "('" + stageName + "' , '" + name + "' , " + birth + ", " + nGold + ", " + nPlat + ");" ;
        try {
            statement.executeUpdate(addQuery);
        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tAdding an Artist (5 param) --> query: " + addQuery);
            e.printStackTrace();
        }
        return addQuery;
    }

    public String addAlbumRecord(String name, String release, boolean gold, boolean plat, int ida) {
        //  Convert boolean to int
        int intGold = gold ? 1 : 0;
        int intPlat = plat ? 1 : 0;

        String addQuery = MessageFormat.format("""
                insert into Album (Name, "Release", Gold, Plat, ida)
                values
                (''{0}'', ''{1}'', {2}, {3}, {4})""", name, release, intGold, intPlat, ida);

        try {
            statement.executeUpdate(addQuery);
        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tAdding an Album --> query: " + addQuery);
            e.printStackTrace();
        }

        return addQuery;
    }

    public String printRs(ResultSet rs) {
        try {
            return Utilities.printRs(rs);
        } catch (SQLException e) {
            System.out.println(Utilities.debHelp() + "ERROR:\tprintRs()");
        }
        return "error";
    }

    public String getMetadata() {
        StringBuilder sb = new StringBuilder();

        try
        {
            DatabaseMetaData dbMd = conn.getMetaData();

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
        }
        catch (SQLException e)
        {
            System.out.println(Utilities.debHelp() + "ERROR:\tGetting metadata from database");
            e.printStackTrace();
        }

        return sb.toString();
    }

}
