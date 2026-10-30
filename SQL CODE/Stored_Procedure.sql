-- Stored procedure for Police_Station_Management
-- Run after SQL CODE.sql. Uses only tables/columns from that schema.

-- Full crime report for one station: every crime recorded there,
-- with the arresting officer and the criminal (if any).
CREATE PROCEDURE sp_GetStationCrimeReport
    @StationID INT
AS
BEGIN
    SELECT
        c.CrimeID,
        c.CrimeDetails,
        c.CrimeDate,
        ps.StationName AS StationName,
        o.OfficerName AS ArrestingOfficer,
        cr.CriminalName AS CriminalName
    FROM Crime c
    JOIN PoliceStation ps ON ps.StationID = c.StationID
    LEFT JOIN Officer o ON o.OfficerID = c.ArrestingOfficerID
    LEFT JOIN Criminal cr ON cr.CriminalID = c.CriminalID
    WHERE c.StationID = @StationID
    ORDER BY c.CrimeDate DESC;
END;
GO
