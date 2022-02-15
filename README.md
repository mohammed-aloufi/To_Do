# ToDo
To Do Android Application using Kotlin.

## Overview: 👀
![ic_launcher](https://user-images.githubusercontent.com/92246795/154055062-48b44deb-bba6-4d05-bf70-5efd32e8697a.png)

This project represents an android application **ToDo**, Where users can manage thier tasks.
> The user can also create tasks based on the custom categories that he creates.

## This app was built using: ⚙️
* MVC Architecture.
* Livedata.
* Room Database.
* Two RecyclerViews & Adapters.

## Libraries 💼
* Room Database Libraries.
* LottieFiles Animation Librariy.

## User interfaces 📱

### Home :
![New Project (9)](https://user-images.githubusercontent.com/92246795/154063431-92b9d863-7bb9-4ddd-9197-08961d3aea79.png)

### New task :
![New Project (10)](https://user-images.githubusercontent.com/92246795/154072055-73cbb790-9ee5-4de8-b648-cde650d477a8.png)

### New/Edit Category :
![New Project (11)](https://user-images.githubusercontent.com/92246795/154072395-c5b7c162-81e2-4d26-a63b-c8416e1935ad.png)

### Task Details :
![New Project (14)](https://user-images.githubusercontent.com/92246795/154073308-d4bfd24f-a62b-46ad-baa8-43d26445a824.png)

### Sorting :
![New Project (15)](https://user-images.githubusercontent.com/92246795/154073422-0e3a3467-06c1-4f2d-966d-ef5d742c7e93.png)

-------------------------------------------------------------------------
## Installation:
Follow the steps below to get started with the project's development environment:
1. Install Android Studio from [Android Studio](https://developer.android.com/studio?gclid=Cj0KCQjw5oiMBhDtARIsAJi0qk2WOPjxp2Wij5sgO3bAK6Rp18zrs4Y0L5S6W89Fk7OClhAiVuNr1mgaAsT-EALw_wcB&gclsrc=aw.ds)
2. Clone this repository:
 ```kotlin 
 $ git clone https://github.com/mohammed-aloufi/To_Do.git
 ```
3. Navigate to the project directory:
 ```kotlin 
 $ cd ToDo
 ```
4. List of the libraries used in the project:
   * for Room Database
 ```kotlin
    apply plugin: 'kotlin-kapt'
    dependencies {
    implementation "androidx.room:room-runtime:2.4.0-beta01"
    kapt "androidx.room:room-compiler:2.4.0-beta01"
    implementation "androidx.room:room-ktx:2.4.0-beta01"
    }
``` 
   * for LottieFiles Animation
 ```kotlin
    dependencies {
    def lottieVersion = "4.2.2"
    implementation "com.airbnb.android:lottie:$lottieVersion"
    }
``` 
 All set 🎉🎉.
 -----------------------------------------------------------------

## Resources:
### Room Database Documentation:
[Go to documentation](https://developer.android.com/jetpack/androidx/releases/room)

### LottieFiles Animation Documentation:
[Go to documentation](https://airbnb.io/lottie/#/android)

```
