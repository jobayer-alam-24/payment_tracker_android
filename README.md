# 💰 Payment Tracker (Android)  
An Android application to track payments — record who owes what, mark payments made/received, and stay on top of your balances.

---

## 🚀 Features  
- Add new payment entries easily.  
- View a complete list of all payments (incoming and outgoing).  
- Mark payments as **Paid/Received** or **Unpaid**.  
- Track your current balance (net of what you owe vs what you’re owed).  
- Clean and simple UI built in **Java** for Android.

---

## 🛠 Tech Stack  
- **Language:** Java  
- **Platform:** Android  
- **Storage:** Local (SQLite / Room)  
- **UI Components:** RecyclerView, Adapters, Buttons, TextViews, etc.

---

## 📂 Project Structure  
/app
├─ src/main/java/com/yourpackage/…
│ ├─ models/ ← Payment model classes
│ ├─ adapters/ ← RecyclerView adapters
│ ├─ activities/ ← Activities (MainActivity, AddPaymentActivity, etc.)
│ ├─ database/ ← Database helper / Room database classes
├─ src/main/res/ ← Layouts, values, drawable resources

## ✅ How to Build & Run  
1. Clone the repository:  
   ```bash
   git clone https://github.com/jobayer-alam-24/payment_tracker_android.git
2. Open the project in Android Studio.

3. Let Android Studio sync Gradle.

4. Connect an Android device or launch an emulator.

5. Run the app.

## Usage

Launch the app to see the payments list.

Tap the “+” button to add a new payment.

Enter the Name, Amount, Type (You owe / You are owed), Date, and optional Notes.

Added payments appear in the list as Unpaid.

Tap a payment to mark it as Paid/Received.

Monitor your net balance at a glance.
