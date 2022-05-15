# wallet_daa_bridge

 Wallet DAA-Bridge is an Android application. 
 Communicates through intents with the DAA wallet application which should be installed in the same device.  

## Requirements
* Android Studio Arctic Fox
* Android version >= 5.0(Lollipop)

## How to Build 
 1) Open the project with Android Studio
 2) Go to Build Variants and select Active Build Variant(Debug or release)	
 3) Build -> Rebuild Project 
 4) Find the generated apk file into the output folder(app\build\outputs\apk)


## How to Run
1) You can use	a virtual device or an android device to run the application in debug mode,
from Android Studio. Otherwise you can copy the apk file in your android device. 
2) Install and then open the application. The text "Installation Succeeded" will be displayed.
3) After installation the Wallet DAA-Bridge can interact with DAA wallet through intents.

## Intent Actions
* eu.door.daa_bridge.REGISTER
* eu.door.daa_bridge.ENABLE
* eu.door.daa_bridge.ISSUE
* eu.door.daa_bridge.SIGN_VC
* eu.door.daa_bridge.SIGN_VP_REQ
* eu.door.daa_bridge.SIGN_VP
