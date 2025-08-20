# ADSPAY
*Empowering Payments, Accelerating Growth, Igniting Innovation*

![last-commit](https://img.shields.io/github/last-commit/dzikfr/AdsPay?style=flat&logo=git&logoColor=white&color=0080ff)
![repo-top-language](https://img.shields.io/github/languages/top/dzikfr/AdsPay?style=flat&color=0080ff)
![repo-language-count](https://img.shields.io/github/languages/count/dzikfr/AdsPay?style=flat&color=0080ff)

### Built with
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=flat&logo=Gradle&logoColor=white)
![XML](https://img.shields.io/badge/XML-005FAD.svg?style=flat&logo=XML&logoColor=white)
![bat](https://img.shields.io/badge/bat-31369E.svg?style=flat&logo=bat&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF.svg?style=flat&logo=Kotlin&logoColor=white)

---

## Table of Contents
- [Overview](#overview)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Testing](#testing)
    - [Structure](#structure)
      - [StructureExplained](#structureexplained)
---

## Overview
ADSPAY is a project aimed at empowering digital payments, accelerating growth, and igniting innovation in fintech solutions.

---

## Getting Started
Lets Start

### Prerequisites
This project requires the following dependencies:

- **Programming Language:** Kotlin
- **Package Manager:** Gradle
- **IDE:** Android Studio
- **Operating System:** Windows, Linux, or macOS
- **Java Version:** 11 or higher
- **Git:** For version control
- **Android SDK: 35 and minimal is 26** For Android development

---

### Installation
Build AdsPay from source and install dependencies:

1. **Clone the repository:**
   ```sh
   git clone https://github.com/dzikfr/AdsPay
   ```

2. **Navigate to the project directory:**
   ```sh
   cd AdsPay
   ```

3. **Build and install dependencies:**
   ```sh
   ./gradlew :app:assembleDebug
   ```

4. **Run the app:**
   ```sh
   ./gradlew :app:runDebug
   ```

---

### Testing
To run the tests, use the following command:
```sh
./gradlew test
```

---

### Structure
The project is structured as follows:

```
.
├── README.md
├── app
│   ├── build.gradle
│   ├── build.gradle.kts
│   ├── src
│   │   ├── main
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── res
│   │   │   │   ├── drawable
│   │   │   │   └── values
│   │   │   └── java
│   │   │       ├── com.example.adspay
│   │   │       │   ├── models
│   │   │       │   ├── navigation
│   │   │       │   ├── screens
│   │   │       │   ├── services
│   │   │       │   ├── ui
│   │   │       │   │   ├── components
│   │   │       │   │   ├── theme
│   │   │       │   ├── utils
│   │   │       │   ├── MainActivity.kt
```

---

#### StructureExplained
- **models :** this folder contains all the data models used in the project
- **navigation :** this folder contains all the navigation components used in the project
- **screens :** this folder contains all the screens used in the project
- **services :** this folder contains all the API services used in the project
- **ui :** there is component folder and theme folder
- **components :** this folder contains all the components used in the project
- **theme :** this folder contains all the theme used in the project
- **utils :** this folder contains all the utils used in the project
- **MainActivity.kt :** this file contains the main activity of the project