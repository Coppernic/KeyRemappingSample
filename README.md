# KeyRemappingSample

Sample application for key remapping on C-One

Introduction
------------
This sample application illustrates how to use the key remapping API on Coppernic C-One and C-One e-ID devices.

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

Key remapping
-------------

### Programmable buttons

On C-One:

|Button|Description|
|---|---|
|P1|Left blue button|
|P2|Right blue button|
|P3|Black button between P1 and Power|

On C-One e-ID:


|Button|Description|
|---|---|
|P1|Left bottom button|
|P2|Right button|
|P3|Left middle button|

### Libraries

CpcCore is the library responsible for power management.

In your build.gradle file, at module level, add the following lines:

```groovy
compile 'fr.coppernic.sdk.core:CpcCore:1.1.1'
```

### Binding / unbinding to the service

As the key remapping is handled by the service CpcSystem services, it is mandatory to bind / unbind to the service before using the key remapping API.

```groovy

@Override
protected void onStart() {
    super.onStart();
    // Binds to service, service manages the key remapping feature
    KeyRemap.getInstance().bindToService(this);
}

@Override
protected void onStop() {
    // Unbinds to service
    KeyRemap.getInstance().unbindToService(this);
    super.onStop();
}

```

### Remap a programmable button to a virtual key

All available virtual key codes are available in a String array:

```groovy

KeyRemap.KEYCODELABELS

```

To remap to a specific virtual key (String):

```groovy

// P1 = 0, P2 = 1, P3 = 2
// virtualKey is a String contained in KeyRemap.KEYCODELABELS
KeyRemap.getInstance().remap(this, KeyRemap.PROGRAMMABLEKEYS[0], virtualKey);

```

### Remap a programmable button to a shortcut

To remap to a specific virtual key (String):

```groovy

// P1 = 0, P2 = 1, P3 = 2
// shortcut is a String representing a shortcut
KeyRemap.getInstance().remap(this, KeyRemap.PROGRAMMABLEKEYS[0], shortcut);

```

### Get current remapping for a programmable button

Method getRemapping is used.

```groovy
// Gets remapping for P1 and displays it in a Toast
KeyRemap.getInstance().getMapping(KeyRemap.PROGRAMMABLEKEYS[0], new OnGetResponseListener() {
    @Override
    public void onGetResponse(final String s) {
        Log.d("TEST", s);

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
});

```

### Remove a remapping for a programmable button

To remove a remapping, method removeMapping is used:

```groovy

// Removes current mapping for P1
KeyRemap.getInstance().removeMapping(this, KeyRemap.PROGRAMMABLEKEYS[0]);

```

### Remove all remappings

To remove all mappings, method removeAllMapping is used:

```groovy

KeyRemap.getInstance().removeAllMapping(this);

```
