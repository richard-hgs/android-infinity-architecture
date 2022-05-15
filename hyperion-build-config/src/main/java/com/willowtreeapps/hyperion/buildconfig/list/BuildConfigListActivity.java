package com.willowtreeapps.hyperion.buildconfig.list;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;

import com.willowtreeapps.hyperion.buildconfig.R;
import com.willowtreeapps.hyperion.buildconfig.model.BuildConfigValue;
import com.willowtreeapps.hyperion.plugin.v1.HyperionIgnore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

@HyperionIgnore
public class BuildConfigListActivity extends AppCompatActivity {

    private static final String TAG = "BuildConfig";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hbc_activity_build_config_list);
        setSupportActionBar((Toolbar) findViewById(R.id.tmb_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.hbc_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new BuildConfigListAdapter(getBuildConfigValues()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String buildConfigName() {
        String resString = getString(R.string.hbc_target_build_config_name);
        if (resString.isEmpty()) {
            return getPackageName() + ".BuildConfig";
        } else {
            return resString;
        }
    }

    private List<BuildConfigValue> getBuildConfigValues() {
        List<BuildConfigValue> buildConfigValues = new LinkedList<>();
        try {
            Class<?> buildConfigClass = Class.forName(buildConfigName());
            Log.d(TAG, "Checking BuildConfig " + buildConfigClass.getName());
            Field[] declaredFields = buildConfigClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                Log.d(TAG, "Inspecting " + declaredField.toString());
                if (Modifier.isStatic(declaredField.getModifiers())) {
                    Class<?> fieldType = declaredField.getType();
                    String name = declaredField.getName() + " (" + fieldType.getSimpleName() + ")";
                    String value = declaredField.get(null).toString();
                    buildConfigValues.add(new BuildConfigValue(name, value));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to read BuildConfig", e);
        }

        return buildConfigValues;
    }

}