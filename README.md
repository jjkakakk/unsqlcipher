unsqlcipher - android hooking for pulling sqlcipher key from blackberry
BES for android
==============

A android application that utilises xposed framework to hook things in
bes to pull out the sqlcipher key (so you can get the password for
encrypted database files).

Idea taken from MDSEC's very good cydia substrate example - this is just
an xposed framework example, specifically for BES. Code can probably be
easily adapted to other things.

Building
--------------

- Built with eclipse, not android dev studio, just build and deploy
- a work in progress, may not be suitable against updated versions of
  bes
- remember to add xposedBridgeApi to build path.

