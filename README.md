# KeyRemappingSample

Sample application for key remapping on C-One

Introduction
------------
This sample application illustrates how to use the ASK UCM108 RFID reader on Coppernic C-One and C-One e-ID devices.

The libraries
-------------

Coppernic uses a Maven repository to provide libraries.

In the build.gradle, at project level, add the following lines:

```groovy
allprojects {
    repositories {                
        maven { url 'https://artifactory.coppernic.fr/artifactory/libs-release'}
    }
}
```
Documentation
-------------

The javadoc for CpcCore can be found [here](https://github.com/Coppernic/coppernic.github.io/raw/master/assets/CpcCore-1.1.1-javadoc.jar).
