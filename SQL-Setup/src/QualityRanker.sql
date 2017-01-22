/*Start by using WhiteHouse.gov*/
USE TrumpGov;

CREATE TABLE query (
    arg VARCHAR(64),
    pos INT
);

CREATE TABLE trumpResults (
    url VARCHAR(256),
    description VARCHAR(MAX),
    relevance INT,
    rank INT
);

/*Add to query and trumpResults*/

/*Start using ObamaWhiteHouse.gov*/
USE ObamaGov;

CREATE TABLE obamaResults (
    url VARCHAR(256),
    description VARCHAR(MAX),
    relevance INT,
    rank INT
);