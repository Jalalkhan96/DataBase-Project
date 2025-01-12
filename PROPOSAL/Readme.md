# Police Station Management System Database Entities

This document outlines the database schema for the **Police Station Management System**, detailing the various entities and their attributes.

---

## 1. PoliceStation
**Purpose**: Stores details about police stations. Each station is uniquely identified by its `StationID`.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| StationID           | INT (PK)    | Unique identifier for each station.      |
| StationName         | VARCHAR(100)| Name of the police station.              |
| StationAddress      | TEXT        | Address of the police station.           |
| City                | VARCHAR(50) | City where the station is located.       |
| StationPhone        | VARCHAR(15) | Contact number of the station.           |
| StationEmail        | VARCHAR(100)| Email address of the station.            |

---

## 2. PoliceIncharge
**Purpose**: Tracks the officer in charge of each police station.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| InchargeID          | INT (PK)    | Unique identifier for the in-charge officer. |
| InchargeName        | VARCHAR(100)| Name of the in-charge officer.           |
| InchargeContact     | VARCHAR(15) | Contact number of the in-charge officer. |
| StationID           | INT (FK)    | ID of the station they are assigned to.  |

---

## 3. Officer
**Purpose**: Stores personal details of all officers in the system.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| OfficerID           | INT (PK)    | Unique identifier for each officer.      |
| OfficerName         | VARCHAR(100)| Full name of the officer.                |
| OfficerRank         | VARCHAR(50) | Rank of the officer (e.g., Inspector, Sub-Inspector). |
| OfficerContact      | VARCHAR(15) | Contact number of the officer.           |
| DateOfJoining       | DATE        | Date the officer joined the force.       |
| OfficerAddress      | TEXT        | Residential address of the officer.      |
| OfficerCity         | VARCHAR(50) | City where the officer resides.          |

---

## 4. OfficerAssignment
**Purpose**: Maps officers to the police stations where they are assigned.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| OfficerID           | INT (FK)    | ID of the officer.                       |
| StationID           | INT (FK)    | ID of the station.                       |
| AssignedDate        | DATE        | Date the officer was assigned to the station. |

---

## 5. Criminal
**Purpose**: Stores personal details of criminals.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| CriminalID          | INT (PK)    | Unique identifier for each criminal.     |
| CriminalName        | VARCHAR(100)| Full name of the criminal.               |
| CriminalDOB         | DATE        | Date of birth of the criminal.           |

---

## 6. Crime
**Purpose**: Tracks crimes committed by criminals, including details of the crime and arresting officers.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| CrimeID             | INT (PK)    | Unique identifier for each crime.        |
| CrimeDetails        | TEXT        | Description of the crime.                |
| CrimeDate           | DATE        | Date the crime was committed.            |
| StationID           | INT (FK)    | ID of the station handling the crime.    |
| ArrestingOfficerID  | INT (FK)    | ID of the officer who made the arrest.   |
| CriminalID          | INT (FK)    | ID of the criminal involved.             |

---

## 7. Case
**Purpose**: Maintains details of all cases registered in the system.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| CaseID              | INT (PK)    | Unique identifier for each case.         |
| CaseTitle           | VARCHAR(100)| Brief title of the case.                 |
| CaseType            | VARCHAR(50) | Type of the case (e.g., Theft, Murder).  |
| CaseDetails         | TEXT        | Detailed description of the case.        |
| CaseStatus          | VARCHAR(50) | Current status (e.g., Open, Closed).     |
| DateReported        | DATE        | Date the case was reported.              |

---

## 8. CaseAssignment
**Purpose**: Tracks which officer is assigned to investigate a case.

| Attribute               | Type        | Description                              |
|-------------------------|-------------|------------------------------------------|
| CaseID                  | INT (FK)    | ID of the case.                          |
| InvestigatingOfficerID  | INT (FK)    | ID of the investigating officer.         |

---

## 9. FIR
**Purpose**: Records all First Information Reports (FIRs) filed by complainants.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| FIRID               | INT (PK)    | Unique identifier for each FIR.          |
| ComplainantID       | INT (FK)    | ID of the complainant filing the FIR.    |
| FIRDetails          | TEXT        | Detailed description of the FIR.         |
| FIRDate             | DATE        | Date the FIR was filed.                  |
| StationID           | INT (FK)    | ID of the police station where the FIR was filed. |

---

## 10. Complainant
**Purpose**: Stores information about complainants.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| ComplainantID       | INT (PK)    | Unique identifier for each complainant.  |
| ComplainantName     | VARCHAR(100)| Full name of the complainant.            |
| ComplainantContact  | VARCHAR(15) | Contact number of the complainant.       |

---

## 11. Complaint
**Purpose**: Tracks complaints filed by citizens at the police station.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| ComplaintID         | INT (PK)    | Unique identifier for each complaint.    |
| ComplaintTitle      | VARCHAR(100)| Brief title of the complaint.            |
| ComplaintDetails    | TEXT        | Detailed description of the complaint.   |
| ComplainantID       | INT (FK)    | ID of the complainant who filed the complaint. |
| StationID           | INT (FK)    | ID of the station where the complaint was filed. |

---

## 12. Evidence
**Purpose**: Tracks evidence collected for cases.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| EvidenceID          | INT (PK)    | Unique identifier for each piece of evidence. |
| EvidenceType        | VARCHAR(50) | Type of evidence (e.g., Weapon, Document). |
| EvidenceDetails     | TEXT        | Description of the evidence.             |
| CaseID              | INT (FK)    | ID of the case the evidence belongs to.  |
| OfficerID           | INT (FK)    | ID of the officer handling the evidence. |

---

## 13. StationOfficer
**Purpose**: Maps police stations to their officers.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| StationID           | INT (FK)    | ID of the station.                       |
| OfficerID           | INT (FK)    | ID of the officer assigned to the station. |

---

## 14. ArrestRecord
**Purpose**: Logs arrests made by officers.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| ArrestID            | INT (PK)    | Unique identifier for each arrest.       |
| CriminalID          | INT (FK)    | ID of the criminal arrested.             |
| ArrestingOfficerID  | INT (FK)    | ID of the arresting officer.             |
| CrimeID             | INT (FK)    | ID of the crime associated with the arrest. |
| ArrestDate          | DATE        | Date the arrest was made.                |

---

## 15. CriminalCaseMapping
**Purpose**: Maps criminals to cases they are involved in.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| CaseID              | INT (FK)    | ID of the case.                          |
| CriminalID          | INT (FK)    | ID of the criminal.                      |

---

## 16. CaseStationMapping
**Purpose**: Tracks which police station is handling a particular case.

| Attribute           | Type        | Description                              |
|---------------------|-------------|------------------------------------------|
| CaseID              | INT (FK)    | ID of the case.                          |
| StationID           | INT (FK)    | ID of the police station.                |

