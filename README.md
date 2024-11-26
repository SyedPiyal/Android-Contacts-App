# Android Contacts App

This is a simple Android application that reads contacts from the device using the `ContactsContract` Content Provider. It requests the necessary permission to read contacts, retrieves contact names (and optionally phone numbers), and displays them in a `RecyclerView`.

## Features:
- Request permission to read contacts.
- Fetch contacts from the device's contact list.
- Display contacts in a `RecyclerView`.
- Handle runtime permission requests for reading contacts.
  
## Requirements:
- Android Studio
- Android 6.0 (API level 23) or higher
- Java 8 or higher

## Permissions:
The app requires the following permissions:
- `READ_CONTACTS`: To access the user's contact data.

You will need to grant this permission at runtime on Android 6.0 (API level 23) or higher. The app will request permission when first launched. If the permission is denied, the app will display a Toast message notifying the user.

## Setup:
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/Android-Contacts-App.git
