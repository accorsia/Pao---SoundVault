package com.unimn.soundvault;

import java.sql.SQLException;

public class DatabaseSafeGetter {
    public static DatabaseManager getDb() {

        DatabaseManager db = null;
        try {
            db = new DatabaseManager();

            //  Update database: "# Gold", "# Plat"
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

        }
        catch (SQLException e)
        {
            System.out.print(Utilities.debHelp() + "> Something went wrong ---> You should restore the database...\n");
            e.printStackTrace();
            //  db.restore();
        }

        return db;
    }
}
