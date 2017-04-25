# MS Test Suite

Front end for the various MS test apps.

## How to integrate a test app

There are three intent actions (in the category `android.intent.category.DEFAULT`) that should be caught by the test app's intent-filters:
- `edu.umd.cmsc436.{...}.action.TRIAL`
- `edu.umd.cmsc436.{...}.action.PRACTICE`
- `edu.umd.cmsc436.{...}.action.HELP`

The possible values in the curly braces are:
- `tap`
- `spiral`
- `balance`
- `level`
- `pop`
- `flex`
- `walk.outdoors`
- `walk.indoors`

For example, the intent to launch the Practice Mode of the Tap app would have the action `edu.umd.cmsc436.tap.action.PRACTICE`.

`.PRACTICE` and `.HELP` don't have any arguments or expect any results.

`.TRIAL` has 5 arguments:
- Appendage, which is a `Sheets.TestType` (from the [sheets436](https://github.com/cmsc436/sheets436) library)
- Trial Num, an integer, the current trial out of this appendage's number of trials, for display purposes
- Trial Out Of, an integer, the current appendage's number of trials, for display purposes
- Patient ID, a string, for storing raw data appropriately
- Difficulty, an integer greater than 0, the lower the easier

There are a few static methods to help with argument extraction as well:
- `getAppendage(Intent)`
- `getTrialNum(Intent)`
- `getTrialOutOf(Intent)`
- `getPatientId(Intent)`
- `getDifficulty(Intent)`

These will help with getting info out of intents delivered with the `.TRIAL` action.

`.TRIAL` also expects a single float result, the score for the trial.  This can be done with the [`Activity#setResult(int, Intent)`](https://developer.android.com/reference/android/app/Activity.html#setResult(int%2c%20android.content.Intent)) method, and the intent from the `TrialMode.getResultIntent(float)` helper static method.

The helper library uses types from the sheets436 library, so make sure to include that as a dependency.  There are also a few color resources defined to help with consistency:
- `colorPrimary436`, orange
- `colorPrimaryDark436`, a slightly darker orange (for the status bar, if present)
- `colorAccent436`, blue
- `colorAccentLight436`, a light blue
- `colorBackground436`, a light gray

These values were taken from [colorcombos.com](http://www.colorcombos.com/color-schemes/89/ColorCombo89.html).

## Prescription Format

One row per patient, with columns:

Patient ID | Date Assigned | Frequency | Column per Test App ... | Status
--- | --- | --- | --- | ---
String | Google Sheets Datetime | Jenkins format (eventually) | Difficulty Integer (0 is disable, 1 is easiest) | Assigned, Ignored, or Completed
