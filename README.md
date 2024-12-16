# eka.news - Android Application

This application is developed as a part of a recruitment assignment for eka.care
This README file provides an overview of the application, including information on technologies used, an APK file, and Demo video.

## Table of Contents
- [Project description](#project-description)
- [Steps to set up the project](#project-setup)
- [Demo Video](#demo-video)
- [Imp Decisions](#decisions)
- [Future Scope](#future-scope)
- [APK Download](#apk-download)

## Project description

- Built with 100% Kotlin
- 100% Offline, works seamlessly without an internet connection (Used RoomDB for data syncronization)
- Added Search feature for quick, personalize article discovery
- Used (MVVM + modular) Architecture design preferred by Google
- Jetpack Compose for designing User Interfaces
- Kotlin Coroutines for Asynchronous work
- Retrofit for making API requests
- Coil: for image loading and caching library
- XML, UI Design, Transitions, and Animations
- Object Oriented Design principles (SOLID principles, Design Patterns, separation of concern, reusability, etc)

## Steps to set up the project

- Used Gradle build system and can be imported directly into Android Studio
- Built with AGP version: 8.7.2 and Gradle version: 8.9

## Demo Video

To see a demonstration of the eka.news, please watch the following demo video:

[Demo Video](https://drive.google.com/file/d/1Sbc1M7G2PlooFmvES8nOtuyYQWOUeqB3/view?usp=sharing)

## Imp Decisions

- Used debouncing effect for search API calls (This saves more than 50% of api requests)
- Used modular architectural approach which saved more than 60% of incremental project build time
- Used data syncronization mechanism using RoomDB for offline access which increases the application's availability

## Future Scope

- Implementing pagination for fetching news articles
- Providing support for article searching in offline through local storage
- Adding support for Dark mode
- etc

## APK Download

To download the release APK of the eka.news Android application, click the link below:

[Download APK](https://drive.google.com/file/d/1srqTRYBeYyETqJzB-328FwUyCpbjKbuz/view?usp=sharing)
