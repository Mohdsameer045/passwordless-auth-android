# Passwordless Authentication App (Android)

This project implements a passwordless authentication flow using **Email + OTP**, followed by a session tracking screen.
It was developed as part of an Android Internship assignment to demonstrate understanding of **Jetpack Compose, ViewModel, state management, and time-based logic**.

---

## 📱 Features

### 1. Email + OTP Login
- User enters an email address
- A 6-digit OTP is generated locally
- OTP is validated without any backend

### 2. OTP Rules Implemented
- OTP length: **6 digits**
- OTP expiry: **60 seconds**
- Maximum attempts: **3**
- Generating a new OTP:
- Invalidates the previous OTP
- Resets attempt count
- OTPs are stored **per email**

### 3. Session Screen
- Displays session start time
- Shows live session duration in **mm:ss**
- Session timer survives recompositions
- Logout ends the session cleanly

---

## 🏗 Architecture & Design

- **UI Layer:** Jetpack Compose (stateless UI)
- **State Management:** ViewModel + StateFlow
- **Architecture Pattern:** One-way data flow
- **Asynchronous Work:** Kotlin Coroutines
- **No global mutable state**
- **No UI logic inside ViewModel**

---

## 🗂 Data Structures Used

### OTP Storage
A `MutableMap<String, OtpData>` is used where:
- Key → User email
- Value → OTP data (otp value, creation time, attempt count)

This structure allows:
- Easy OTP invalidation
- Email-specific OTP tracking
- Clean enforcement of expiry and attempt limits

---

## ⏱ OTP Expiry & Time Handling

- OTP expiry is enforced using `System.currentTimeMillis()`
- ViewModel handles expiry logic using coroutines
- UI countdown timer is derived from expiry timestamp
- Session duration is calculated from a fixed session start timestamp

This ensures timers:
- Survive recompositions
- Remain accurate during screen rotations

---

## 📊 External SDK Integration

**SDK Used:** Timber
**Reason:** Lightweight logging library suitable for local event tracking without backend setup.

### Logged Events:
- OTP generated
- OTP validation success
- OTP validation failure
- Logout

Timber is initialized in the custom `Application` class.

---

## 🤖 AI Assistance Disclosure

GPT was used for:
- Clarifying Jetpack Compose patterns
- Verifying best practices for ViewModel and state management
- Reviewing architecture decisions

All core logic, structure, and implementation decisions were:
- Understood
- Manually implemented
- Verified through testing

---

## ⚙ Setup Instructions

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle
4. Run on emulator or physical device (API 24+)

No additional configuration required.

---

## 🧪 Edge Cases Handled

- Expired OTP
- Incorrect OTP
- Maximum attempts exceeded
- OTP resend flow
- Screen rotation without state loss

---

## ✅ Assignment Compliance Checklist

- [x] Jetpack Compose
- [x] ViewModel + UI State
- [x] Coroutines
- [x] OTP rules enforced
- [x] Session timer
- [x] External SDK integration
- [x] README documentation

---

## 📹 Demo Video

A short demo video showing:
- OTP generation
- Validation flow
- Session timer
- Logout behavior

(Uploaded as per submission instructions)
