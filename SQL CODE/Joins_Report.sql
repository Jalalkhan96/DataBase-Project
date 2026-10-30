-- Join reports for Police_Station_Management
-- Run after SQL CODE.sql. Uses only tables/columns from that schema.

-- Report 1: every case with its investigating officer and station
-- (stations reach cases through CaseStationMapping).
SELECT
    cs.CaseID,
    cs.CaseTitle,
    cs.CaseType,
    cs.CaseStatus,
    cs.DateReported,
    o.OfficerName AS InvestigatingOfficer,
    ps.StationName AS StationName
FROM Cases cs
LEFT JOIN CaseAssignment ca ON ca.CaseID = cs.CaseID
LEFT JOIN Officer o ON o.OfficerID = ca.InvestigatingOfficerID
LEFT JOIN CaseStationMapping csm ON csm.CaseID = cs.CaseID
LEFT JOIN PoliceStation ps ON ps.StationID = csm.StationID
ORDER BY cs.DateReported DESC;
GO

-- Report 2: every crime with its station, arresting officer and criminal.
SELECT
    c.CrimeID,
    c.CrimeDetails,
    c.CrimeDate,
    ps.StationName AS StationName,
    o.OfficerName AS ArrestingOfficer,
    cr.CriminalName AS CriminalName
FROM Crime c
LEFT JOIN PoliceStation ps ON ps.StationID = c.StationID
LEFT JOIN Officer o ON o.OfficerID = c.ArrestingOfficerID
LEFT JOIN Criminal cr ON cr.CriminalID = c.CriminalID
ORDER BY c.CrimeDate DESC;
GO
