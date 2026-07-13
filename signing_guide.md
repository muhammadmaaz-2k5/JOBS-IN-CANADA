# Android - Generating a Signed Release AAB (Android App Bundle)

To upload your app bundle to the Google Play Store, it must be signed with a production release keystore (Google Play rejects bundles signed with default debug keys).

Follow these simple steps in **Android Studio** to generate your signed release bundle:

---

### Step 1: Open the project in Android Studio
Make sure you have the `JOBSINCANADA` project open in Android Studio.

### Step 2: Generate Signed Bundle
1. In the top menu, go to: **Build** -> **Generate Signed Bundle / APK...**
2. Select **Android App Bundle** and click **Next**.

### Step 3: Create a Keystore (If you don't have one)
1. Under **Key store path**, click **Create new...**
2. Choose a secure location on your computer to save the keystore file (e.g. `c:\Users\asdfq\Desktop\job_in_canada_keystore.jks`).
3. Enter passwords for the **Key store** and the **Key (Alias)**. *Make sure to write these passwords down safely!*
4. Enter an **Alias** name (e.g. `upload-key`) and fill in the certificate details (First and Last Name, Organization, etc.).
5. Click **OK**, then click **Next**.

### Step 4: Build the Release AAB
1. Under **Destination Folder**, choose where you want to save the final AAB.
2. Select the **release** Build Variant.
3. Click **Create** (or **Finish**).

---

### Where is the signed AAB?
Android Studio will build the app and notify you when it's done. You will find the signed `.aab` file in the folder you selected in Step 4 (usually inside `app/release/app-release.aab`), which you can upload directly to the Google Play Console!
