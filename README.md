# Android Workout App
## How to run the project
Since this application is not published on the Play Store it can only be run by downloading the project and running it with Android Studio:

1. Download and Install Android Studio
2. Clone the repository using SSH `git clone git@github.com:MarjanaKarzek/android-workout-app.git`
3. In Android Studio open the project
4. Run the project on a device or emulator
5. Enjoy the app <3

### API
The app is using the [WGER Workout Manager](https://wger.de/de/software/api) api. There is no api key needed to access it.

## Teck Stack
### Patterns
Clean Architecture

MVVM

Repository Pattern

### Technologies
Kotlin


Dagger Android

RxJava2 with RxKotlin, RxBinding, AutoDispose

AndroidX


Gson

OkHttp

Retrofit


Room


Material Components

Intuit

Glide

RecyclerView Animator


JUnit5

Mockk

Espresso with JUnit4


Timber

## Tools
Chucker
LeakCanary

## Remarks
There is a memory leak in the debug version of the app due to Chucker: 

[More information](https://github.com/ChuckerTeam/chucker/issues/102)
