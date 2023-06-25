package com.unimn.soundvault;
import java.sql.*;

public class DatabaseManager {
    protected Connection conn;
    protected Statement statement;

    public DatabaseManager() throws SQLException {
        /*
        1)  Load vendor specific class

        Class.forName("org.sqlite.jdbc");
         */

        System.out.print(Utilities.debHelp() + "> Connecting to database...");  //  debug

        //  2)  Establish a connection
        conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");

        //  3)  Create JDBC Statement
        statement = conn.createStatement();

        System.out.println("OK!");  //  debug
    }

    public void close() throws SQLException {
        if (conn != null) {
            statement.close();
            conn.close();
        }

        System.out.print(Utilities.debHelp() + "> Closing database...\n");  //  debug

    }

    //  Restore db. with 3 base record
    public void restore() throws SQLException {
        statement.executeQuery("drop table if exists Artist");
        statement.executeQuery("""
                create table Artist (
                    ida serial primary key,
                    "Stage Name" varchar not null,
                    Birth date not null,
                    "# Gold" integer default 0,
                    "# Plat" integer default 0,
                    Name varchar
                );
                """);

        //  add 3 base record
        statement.executeQuery("""
                insert into Artist (ida, "Stage Name", Birth, "# Gold", "# Plat", Name)
                values
                    (1, 'Sfera Ebbasta', '1990-01-01', 0, 0, 'Gionata Boschetti'),
                    (2, 'Tedua', '1993-05-15', 0, 0, 'Mario Mariotti'),
                    (3, 'Mirko', '1994-07-12', 0, 0, 'Mirco Ferrari');
                """);

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

    //  print()
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
