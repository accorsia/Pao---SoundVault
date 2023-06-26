UPDATE Artist SET
    `# Gold` = (
    SELECT COUNT(*)
    FROM Album
    WHERE Album.ida = Artist.ida
      AND Album.Gold = 1
),
    `# Plat` = (
        SELECT COUNT(*)
        FROM Album
        WHERE Album.ida = Artist.ida
          AND Album.Plat = 1
    );
