Create database Police_Station_Management

use Police_Station_Management
Go


CREATE TABLE PoliceStation (
    StationID INT PRIMARY KEY,
    StationName VARCHAR(100),
    StationAddress TEXT,
    City VARCHAR(50),
    StationPhone VARCHAR(15),
    StationEmail VARCHAR(100)
);

CREATE TABLE PoliceIncharge (
    InchargeID INT PRIMARY KEY,
    InchargeName VARCHAR(100),
    InchargeContact VARCHAR(15),
    StationID INT,
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID)
);

CREATE TABLE Officer (
    OfficerID INT PRIMARY KEY,
    OfficerName VARCHAR(100),
    OfficerRank VARCHAR(50),
    OfficerContact VARCHAR(15),
    DateOfJoining DATE,
    OfficerAddress TEXT,
    OfficerCity VARCHAR(50)
);

CREATE TABLE OfficerAssignment (
    OfficerID INT,
    StationID INT,
    AssignedDate DATE,
    FOREIGN KEY (OfficerID) REFERENCES Officer(OfficerID),
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID),
    PRIMARY KEY (OfficerID, StationID)
);


CREATE TABLE Criminal (
    CriminalID INT PRIMARY KEY,
    CriminalName VARCHAR(100),
    CriminalDOB DATE
);

CREATE TABLE Crime (
    CrimeID INT PRIMARY KEY,
    CrimeDetails TEXT,
    CrimeDate DATE,
    StationID INT,
    ArrestingOfficerID INT,
    CriminalID INT,
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID),
    FOREIGN KEY (ArrestingOfficerID) REFERENCES Officer(OfficerID),
    FOREIGN KEY (CriminalID) REFERENCES Criminal(CriminalID)
);

CREATE TABLE Cases (
    CaseID INT PRIMARY KEY,
    CaseTitle VARCHAR(100),
    CaseType VARCHAR(50),
    CaseDetails TEXT,
    CaseStatus VARCHAR(50),
    DateReported DATE
);

CREATE TABLE CaseAssignment (
    CaseID INT,
    InvestigatingOfficerID INT,
    FOREIGN KEY (CaseID) REFERENCES Cases(CaseID),
    FOREIGN KEY (InvestigatingOfficerID) REFERENCES Officer(OfficerID),
    PRIMARY KEY (CaseID, InvestigatingOfficerID)
);

CREATE TABLE FIR (
    FIRID INT PRIMARY KEY,
    ComplainantID INT,
    FIRDetails TEXT,
    FIRDate DATE,
    StationID INT,
    FOREIGN KEY (ComplainantID) REFERENCES Complainant(ComplainantID),
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID)
);

CREATE TABLE Complainant (
    ComplainantID INT PRIMARY KEY,
    ComplainantName VARCHAR(100),
    ComplainantContact VARCHAR(15)
);

CREATE TABLE Complaint (
    ComplaintID INT PRIMARY KEY,
    ComplaintTitle VARCHAR(100),
    ComplaintDetails TEXT,
    ComplainantID INT,
    StationID INT,
    FOREIGN KEY (ComplainantID) REFERENCES Complainant(ComplainantID),
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID)
);

CREATE TABLE Evidence (
    EvidenceID INT PRIMARY KEY,
    EvidenceType VARCHAR(50),
    EvidenceDetails TEXT,
    CaseID INT,
    OfficerID INT,
    FOREIGN KEY (CaseID) REFERENCES Cases(CaseID),
    FOREIGN KEY (OfficerID) REFERENCES Officer(OfficerID)
);

CREATE TABLE StationOfficer (
    StationID INT,
    OfficerID INT,
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID),
    FOREIGN KEY (OfficerID) REFERENCES Officer(OfficerID),
    PRIMARY KEY (StationID, OfficerID)
);

CREATE TABLE ArrestRecord (
    ArrestID INT PRIMARY KEY,
    CriminalID INT,
    ArrestingOfficerID INT,
    CrimeID INT,
    ArrestDate DATE,
    FOREIGN KEY (CriminalID) REFERENCES Criminal(CriminalID),
    FOREIGN KEY (ArrestingOfficerID) REFERENCES Officer(OfficerID),
    FOREIGN KEY (CrimeID) REFERENCES Crime(CrimeID)
);


CREATE TABLE CriminalCaseMapping (
    CaseID INT,
    CriminalID INT,
    FOREIGN KEY (CaseID) REFERENCES Cases(CaseID),
    FOREIGN KEY (CriminalID) REFERENCES Criminal(CriminalID),
    PRIMARY KEY (CaseID, CriminalID)
);

CREATE TABLE CaseStationMapping (
    CaseID INT,
    StationID INT,
    FOREIGN KEY (CaseID) REFERENCES Cases(CaseID),
    FOREIGN KEY (StationID) REFERENCES PoliceStation(StationID),
    PRIMARY KEY (CaseID, StationID)
);



select * from Officer
select * from Criminal
select * from Evidence

select * from Cases
select * from Crime
select * from PoliceStation
select * from PoliceIncharge
select * from ArrestRecord
select * from StationOfficer
select * from OfficerAssignment

SELECT c.CaseTitle, c.CaseStatus, o.OfficerName AS InvestigatingOfficer, cr.CriminalName AS InvolvedCriminal FROM Cases c JOIN CaseAssignment ca ON c.CaseID = ca.CaseID JOIN Officer o ON ca.InvestigatingOfficerID = o.OfficerID JOIN CriminalCaseMapping ccm ON c.CaseID = ccm.CaseID JOIN Criminal cr ON ccm.CriminalID = cr.CriminalID  WHERE c.CaseStatus = 'Solved' AND c.DateReported > '2012-01-01';