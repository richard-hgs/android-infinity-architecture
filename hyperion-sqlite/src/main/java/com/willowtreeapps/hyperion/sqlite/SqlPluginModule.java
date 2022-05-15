package com.willowtreeapps.hyperion.sqlite;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.hyperion.plugin.v1.PluginModule;
import com.willowtreeapps.hyperion.sqlite.presentation.database.DatabaseListActivity;

class SqlPluginModule extends PluginModule implements View.OnClickListener {

    @Nullable
    @Override
    public View createPluginView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.hsql_item_plugin, parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        getContext().startActivity(new Intent(getContext(), DatabaseListActivity.class));
    }
}