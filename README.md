# MS Test Suite

Front end for the various MS test apps.

## How to integrate a test app

## Prescription Format

One row per patient, with columns:

Patient ID | Date Assigned | Frequency | Column per Test App ... | Status
--- | --- | --- | --- | ---
String | Google Sheets Datetime | Jenkins format (eventually) | Difficulty Integer (0 is disable, 1 is easiest) | Assigned, Ignored, or Completed
