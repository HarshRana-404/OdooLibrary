---


# Library Management System

## Description
A comprehensive Library Management System that allows users to manage book inventories, track borrower details, and handle transactions efficiently.

## Screenshots


<img src="https://github.com/user-attachments/assets/065ad772-82b3-4968-8182-e1d545037989" width="214" height="464" />  &emsp; &ensp;
<img src="https://github.com/user-attachments/assets/98a202d0-2ae1-4531-9c13-f9f4cfbf8e1d" width="214" height="464" />  &emsp; &ensp;
<img src="https://github.com/user-attachments/assets/8547c89e-977c-4d63-87f8-6963ce16d9af" width="214" height="464" />  &emsp; &ensp;
<img src="https://github.com/user-attachments/assets/ca3739a6-5817-4333-9b66-51bfd7e65b81" width="214" height="464" />  &emsp; &ensp;
<img src="https://github.com/user-attachments/assets/a6859595-d76c-4dc4-87a0-0859a29ba369" width="214" height="464" />  &emsp; &ensp;
<img src="https://github.com/user-attachments/assets/3cd24757-6cd6-49a9-b66c-d32ce978b3d3" width="214" height="464" />  &emsp; &ensp;

## Features

### User Management
- **Login/Logout functionality** for Admin and Users.
- **Role-based access control**: Admin, Librarian, and User roles.

### Book Inventory Management
- **Add, update, delete, and search for books.**
- **Book details**: ISBN, title, author, publisher, year, genre, quantity.
- **Real-time availability status.**
- Integration with **Google Books API** to fetch book details using ISBN.
  
### Borrowing System
- **Checkout process** for borrowing books.
- **Return process** including due dates and late fees calculation.
- **History tracking** for each user's borrowed and returned books.

### Search and Recommendations
- **Advanced search options** (by title, author, genre, etc.).
- **Book recommendations** based on user history or popular trends.

### Notifications and Alerts
- **Email or SMS notifications** for due dates, new arrivals, etc.
- **Alerts for overdue books and outstanding fees.**

### Reporting
- Generate **reports** on book usage, overdue items, user activity, etc.
- **Dashboard** for admins and librarians to see real-time statistics.

## Submission Requirements
- A working prototype of the Books Library Management System.
- Access to a repository with complete source code for evaluation.
- A brief presentation or video demo outlining key features and technologies used.

## Setup Instructions

### Prerequisites
- **Android Studio** (latest version)
- **Java Development Kit (JDK)** 8 or higher
- **Firebase** account for authentication and Firestore database

### Installation

1. **Clone the repository**:
    ```bash
   https://github.com/HarshRana-404/OdooLibrary.git
    ```

2. **Open the project in Android Studio**:
    - Start Android Studio.
    - Select `Open an existing Android Studio project`.
    - Navigate to the cloned repository directory and open it.

3. **Configure Firebase**:
    - Go to the [Firebase Console](https://console.firebase.google.com/).
    - Create a new project or select an existing project.
    - Add an Android app to your project.
    - Register your app with the package name `com.threebrains.odoolibrary`.
    - Download the `google-services.json` file and place it in the `app` directory of your project.
    - Enable Firestore and Firebase Authentication in the Firebase Console.
      ```
    - Sync the project with Gradle files.
    - Connect an Android device or start an emulator.
    - Click the `Run` button in Android Studio.

### System Requirements
- **Operating System**: Windows, macOS, or Linux
- **RAM**: 4 GB minimum (8 GB recommended)
- **Storage**: 2 GB for Android Studio + additional space for Android SDK, Emulator system images, and project files

## Usage
- **Admin**: Manages users, book inventory, and generates reports.
- **Librarian**: Manages book transactions and assists users.
- **User**: Searches for books, borrows, and returns books.

## Technologies Used
- **Android SDK**
- **XML**
- **Java**
- **Firebase Authentication**
- **Firebase Firestore**
- **Firebase Storage**
- **Material Design Components**


## Repository
-https://github.com/HarshRana-404/OdooLibrary.git

## License
- This project is licensed under the MIT License - see the LICENSE.md file for details.

---

