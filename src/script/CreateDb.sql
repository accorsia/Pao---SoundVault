-- we don't know how to generate root <with-no-name> (class Root) :(
create table Artist
(
    ida          integer not null
        primary key autoincrement,
    "Stage Name" varchar not null,
    Birth        varchar not null,
    "# Gold"     integer default 0,
    "# Plat"     integer default 0,
    Name         varchar
);

create table Album
(
    idb       integer not null
        primary key autoincrement,
    Name      varchar not null,
    "Release" date,
    Gold      boolean,
    Plat      boolean,
    ida       integer
        constraint fk_ida
            references Artist
            on delete cascade
);

