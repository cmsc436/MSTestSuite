# MS Test Suite

Front end for the various MS test apps.

## How to integrate a test app

There are three intent actions that should be caught:
- `<your package name>.action.TRIAL`
- `<your package name>.action.PRACTICE`
- `<your package name>.action.HELP`

For example, if MS Test Suite reacted to Practice Mode, it would catch intents with the `edu.umd.cmsc436.mstestsuite.action.PRACTICE` action.  This way, a specific test and mode can be simulateously specified.

`.PRACTICE` and `.HELP` don't have any arguments or expect any results.

`.TRIAL` has 4 arguments:
- Appendage, which is a `Sheets.TestType` (from the [sheets436](https://github.com/cmsc436/sheets436) library)
- Trial Num, an integer, the current trial out of this appendage's number of trials, for display purposes
- Trial Out Of, an integer, the current appendage's number of trials, for display purposes
- Patient ID, a string, for storing raw data appropriately

`.TRIAL` also expects a single float result, the score for the trial.  This can be done with the [`Activity#setResult(int, Intent)`](https://developer.android.com/reference/android/app/Activity.html#setResult(int%2c%20android.content.Intent)) method, and the intent from the `TrialMode.getResultIntent(float)` helper static method.

There are a few static methods to help with argument extraction as well:
- `getAppendage(Intent)`
- `getTrialNum(Intent)`
- `getTrialOutOf(Intent)`
- `getPatientId(Intent)`

These will help with getting info out of intents delivered with the `.TRIAL` action.

The helper library uses types from the sheets436 library, so make sure to include that as a dependency.  There are also a few color resources defined to help with consistency:
- `colorPrimary436`, orange
- `colorPrimaryDark436`, a slightly darker orange (for the status bar, if present)
- `colorAccent436`, blue
- `colorAccentLight436`, a light blue
- `colorBackground436`, a light gray

These value were taken from [colorcombos.com](http://www.colorcombos.com/color-schemes/89/ColorCombo89.html).

## Prescription Format

One row per patient, with columns:

Patient ID | Date Assigned | Frequency | Column per Test App ... | Status
--- | --- | --- | --- | ---
String | Google Sheets Datetime | Jenkins format (eventually) | Difficulty Integer (0 is disable, 1 is easiest) | Assigned, Ignored, or Completed
