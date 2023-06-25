package com.unimn.soundvault;

import java.sql.SQLException;

public class DatabaseSafeGetter {
    public static DatabaseManager main() {

        DatabaseManager db = null;
        try {
            System.out.print(Utilities.debHelp() + "> Connecting to database...");
            db = new DatabaseManager();
            System.out.println("OK!");

            //  Update database: "# Gold", "# Plat"
            System.out.print(Utilities.debHelp() + "> Updating database...");
            db.executeUpdate("""
                    UPDATE Artist
                    SET `# Gold` = (
                        SELECT COUNT(*)\s
                        FROM Album\s
                        WHERE Album.ida = Artist.ida\s
                        AND Album.Gold = 1
                    ),
                    `# Plat` = (
                        SELECT COUNT(*)\s
                        FROM Album\s
                        WHERE Album.ida = Artist.ida\s
                        AND Album.Plat = 1
                    );""");
            System.out.println("OK!");
        }
        catch (SQLException e)
        {
            System.out.print(Utilities.debHelp() + "> Something went wrong ---> Restoring database...\n");
            e.printStackTrace();
            //  db.restore();
        }

        return db;
    }
}
