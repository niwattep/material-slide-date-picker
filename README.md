# Material Slide Date Picker

[![](https://jitpack.io/v/niwattep/material-slide-date-picker.svg)](https://jitpack.io/#niwattep/material-slide-date-picker)

A slide date picker for Android.

![alt text](https://github.com/niwattep/material-slide-date-picker/blob/master/image/image.jpg)

## Prerequisites

This library uses [AndroidX library](https://developer.android.com/jetpack/androidx). If your project uses Android Support Library, consider [migrating your project to AndroidX](https://developer.android.com/jetpack/androidx/migrate).

## Installation

Add this to project level `build.gradle`

```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency into you app level `build.gradle`

```gradle
implementation 'com.github.niwattep:material-slide-date-picker:v2.0.0'  
```

## Usage

### Show Dialog

Slide Date Picker implements DialogFragment so that it will retain its state when configuration change happens. You can show the dialog by using supportFragmentManager of an activity (or childFragmentManager of a fragment).

```kotlin
SlideDatePickerDialog.newInstance().show(supportFragmentManager, "TAG")
```

You can show the dialog using its builder.

```kotlin
SlideDatePickerDialog.Builder().build().show(supportFragmentManager, "TAG")
```

### Callback

Your activity or fragment must implement interface `SlideDatePickerDialogCallback`.

```kotlin
class MainActivity : AppCompatActivity(), SlideDatePickerDialogCallback {
    override fun onPositiveClick(day: Int, month: Int, year: Int, calendar: Calendar) {
        tvDate.text = SimpleDateFormat("EEEE, MMM dd, yyyy").format(calendar.time)
    }
```

## Customization

```kotlin
SlideDatePickerDialog.Builder()
  .setStartDate(startCalendar)
  .setEndDate(endCaledar)
  .setPreselectedDate(todayCalendar)
  .setYearModifier(543)
  .setLocale(Locale("th"))
  .setThemeColor(Color.RED)
  .setHeaderTextColor(Color.WHITE)
  .setHeaderDateFormat("EEE dd MMMM")
  .setShowYear(true)
  .setCancelText("Cancel")
  .setConfirmText("Confirm")
  .build()
  .show(supportFragmentManager, "TAG")
```

There are 11 things you can customize: 
1. `StartDate` - `Calendar` object of minimum available date (default value is January 1st, current year - 100)
2. `EndDate` - `Calendar` object of maximum available date (default value is `Calendar.getInstance()`)
3. `PreselectedDate` - Date that is selected when start the dialog (dafault value is `Calendar.getInstance()`)
4. `YearModifier` - When you want to show year in diffent format (default is 0)
5. `Locale` - Locale for formatting date (default is `Locale.US`)
6. `ThemeColor` - Color of dialog
7. `HeaderTextColor` - Color of display date
8. `HeaderDateFormat` - Format of display date
9. `ShowYear` - Show or hide year of display date
10. `CancelText` - String of cancel button
11. `ConfirmText` - String of confirm button



