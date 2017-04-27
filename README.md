# Getting Started for debuggers and people working on this project

You need Github account and Android Studio (and all of its multifarious tools) to start working on this project.

Fork this repo, git clone your forked repo into Android Studio to start working.

Please read this before getting started: https://docs.google.com/document/d/1lUSrak2QK2b98u6sNYeuld0qSeWKvI5auOkvWeh_ips/edit?usp=sharing


## WTF am I looking at?

You are looking at a complex application structure.


## Urm...can you at least tell me a bit what's going on?

DrawerActivity.java calls on Triage and Consultation Fragment. We're not sure about Pharmacy. When you click on triage, triage fragment is loaded. When you click on Consultation, consultation fragment is called (see LandingPageActivity.java and DrawerActivity.java listenIntentMethod() )


## What can I do?

Here are three critical bugs:
1. Code is not optimized, causing the app to crash when a mobile device uses up all its RAM i.e moving from fragment to fragment to quickly.
2. Why the hell are we moving from Consultation to Pharmacy "Finished" tab, skipping medications?
3. Crashing when we're trying to edit patient's information in Consultation. This is becausae too much resources are being called onClick (crazy amount of RAM used).

You could try to fix them, or build a new app that takes on a basic form of current app.

Here's SIGHT team's vision of the app: https://assets.adobe.com/link/b70f77e2-464e-4f32-5c72-62114c3a50d1?section=activity_public&page=1


## Other information

https://github.com/hkust1516csefyp43/ehr-server

```
            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
                    Version 2, December 2004

 Copyright (C) 2004 HKUST 2015-2016 CSE FYP Group 43 
 <hkust1516csefyp43@gmail.com>

 Everyone is permitted to copy and distribute verbatim or modified
 copies of this license document, and changing it is allowed as long
 as the name is changed.

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

  0. You just DO WHAT THE FUCK YOU WANT TO.

```