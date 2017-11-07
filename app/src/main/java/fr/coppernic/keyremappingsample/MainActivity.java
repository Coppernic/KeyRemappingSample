package fr.coppernic.keyremappingsample;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.coppernic.sdk.keyremapper.cone.KeyRemap;
import fr.coppernic.sdk.keyremapper.cone.OnGetResponseListener;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.spProgrammableButtons) public Spinner spProgrammableButtons;
    @BindView(R.id.spVirtualKeys) public  Spinner spVirtualKeys;
    @BindView(R.id.spShortcuts) public  Spinner spShortcuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initSpinners();
    }

    /**
     * Fills the virtual keys and shortcuts spinners
     */
    private void initSpinners() {
        // Spinner virtual keys
        ArrayAdapter<String> adapterVirtualKeys = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                KeyRemap.KEYCODELABELS);
        adapterVirtualKeys.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVirtualKeys.setAdapter(adapterVirtualKeys);

        // Spinner shortcuts
        ArrayAdapter<String> adapterShortcuts = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getShortcuts());
        adapterShortcuts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spShortcuts.setAdapter(adapterShortcuts);
    }

    /**
     * Returns the list of all the applications remappable
     * @return List of all the applications remappable
     */
    private String[] getShortcuts() {
        List<ResolveInfo> apps;
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pManager = this.getPackageManager();
        apps = pManager.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

        String[] shortcuts = new String[apps.size()];
        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo resolve = apps.get(i);
            shortcuts[i] = (String) resolve.loadLabel(pManager);
        }

        return shortcuts;
    }

    /**
     * Remaps the currently selected programmable key to the currently selected virtual key
     */
    @OnClick(R.id.btnRemapToVirtualKey)
    public void remapToVirtualKey() {
        int programmableKeyPosition = spProgrammableButtons.getSelectedItemPosition();
        String virtualKey = spVirtualKeys.getSelectedItem().toString();
        KeyRemap.getInstance().remap(this, KeyRemap.PROGRAMMABLEKEYS[programmableKeyPosition], virtualKey);
    }

    /**
     * Remaps the currently selected programmable key to the currently selected application
     */
    @OnClick(R.id.btnRemapToShortcut)
    public void remapToShortcut() {
        int programmableKeyPosition = spProgrammableButtons.getSelectedItemPosition();
        String shortcut = spShortcuts.getSelectedItem().toString();
        KeyRemap.getInstance().remapShortcut(this, KeyRemap.PROGRAMMABLEKEYS[programmableKeyPosition], shortcut);
    }

    /**
     * Gets current remapping for the currently selected programmable key
     */
    @OnClick(R.id.btnGetRemapping)
    public void getRemapping() {
        int programmableKeyPosition = spProgrammableButtons.getSelectedItemPosition();
        KeyRemap.getInstance().getMapping(KeyRemap.PROGRAMMABLEKEYS[programmableKeyPosition], new OnGetResponseListener() {
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
    }

    /**
     * Removes remapping for the currently selected programmable key
     */
    @OnClick(R.id.btnRemoveRemapping)
    public void removeRemapping() {
        int programmableKeyPosition = spProgrammableButtons.getSelectedItemPosition();
        KeyRemap.getInstance().removeMapping(this, KeyRemap.PROGRAMMABLEKEYS[programmableKeyPosition]);
    }

    /**
     * Removes all remappings
     */
    @OnClick(R.id.btnRemoveAllRemappings)
    public void removeAllRemappings() {
        KeyRemap.getInstance().removeAllMapping(this);
    }

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
}
