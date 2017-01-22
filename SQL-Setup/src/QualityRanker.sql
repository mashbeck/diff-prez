/*Start by using WhiteHouse.gov*/
USE TrumpGov;

CREATE TABLE query (
    arg VARCHAR(64),
    pos INT
);

CREATE TABLE trumpResults (
    url VARCHAR(256),
    description TEXT(65535),
    relevance INT,
    ranking INT
);

/*Add to query and trumpResults*/

/*Start using ObamaWhiteHouse.gov*/
USE ObamaGov;

CREATE TABLE obamaResults (
    url VARCHAR(256),
    description TEXT(65535),
    relevance INT,
    ranking INT
);

/*DO THIS AFTER PROCESSING QUERY AND FETCHING SIGNIFICANT RESULTS*/

DROP TABLE obamaResults;

USE TrumpGov;

DROP TABLE trumpResults;

DROP TABLE query;