---


# Library Management System

## Description
A comprehensive Library Management System that allows users to manage book inventories, track borrower details, and handle transactions efficiently.

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
- **Java**
- **Firebase Authentication**
- **Firebase Firestore**
- **Google Books API**
- **Material Design Components**

## Demo
- Video 

## Repository
-https://github.com/HarshRana-404/OdooLibrary.git

## License
- This project is licensed under the MIT License - see the LICENSE.md file for details.

---

