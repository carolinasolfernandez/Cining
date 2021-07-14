# Cining

## Simple Kotlin App
It consists on a CRUD for places.
Registered users can create a place and geo locate it.
This app also allows register users to use Google authorization.
The places contains a title, address, website, geolocation and a photo.
The app also allows the user to change some settings such as the language and the theme.

This app implements:
- Firestore
- Google Authentication with email/password
- Google Storage
- GeoLocation
- Google maps

## Run it
1. Create a Firebase account on Google [[link]](https://console.firebase.google.com)
2. Activate Firestore, Authentication, Storage on Firebase
3. Add the google-services.json file to the project
4. Activate Google maps SDK for Android
5. Add the google_maps_api-xml file with the secret key to app/main/res/values/
6. Build & Run the app

## App

| Login | List |
| :---: | :---: |
| ![Login](./docs/login.png) | ![Place List](./docs/list.png) |
| Create | Map |
| ![Create](./docs/create.png) | ![Map](./docs/map.png) |
| Settings|  |
| ![Settings](./docs/settings.png) |  |
