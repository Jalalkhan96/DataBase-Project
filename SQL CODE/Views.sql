-- Views for Police_Station_Management
-- Run after SQL CODE.sql. Uses only tables/columns from that schema.

-- Crimes per station, with the latest crime date at each station.
CREATE VIEW vw_CrimeByStation AS
SELECT
    ps.StationID,
    ps.StationName,
    ps.City,
    COUNT(c.CrimeID) AS TotalCrimes,
    MAX(c.CrimeDate) AS LatestCrimeDate
FROM PoliceStation ps
LEFT JOIN Crime c ON c.StationID = ps.StationID
GROUP BY ps.StationID, ps.StationName, ps.City;
GO

-- Open (not closed) cases per investigating officer.
CREATE VIEW vw_OfficerOpenCaseLoad AS
SELECT
    o.OfficerID,
    o.OfficerName,
    o.OfficerRank,
    COUNT(ca.CaseID) AS OpenCases
FROM Officer o
JOIN CaseAssignment ca ON ca.InvestigatingOfficerID = o.OfficerID
JOIN Cases cs ON cs.CaseID = ca.CaseID
WHERE cs.CaseStatus <> 'Closed'
GROUP BY o.OfficerID, o.OfficerName, o.OfficerRank;
GO
