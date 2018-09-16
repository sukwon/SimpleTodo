package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String text = getIntent().getStringExtra("text");
        position = getIntent().getIntExtra("position", 0);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(text);
        etEditItem.requestFocus();
    }

    public void onSaveItem(View v) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemText = etEditItem.getText().toString();
        Intent data = new Intent();
        data.putExtra("text", itemText);
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        finish();
    }
}
