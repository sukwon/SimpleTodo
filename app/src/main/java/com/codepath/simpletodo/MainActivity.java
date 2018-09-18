package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new TodoItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        TodoItem todoItem = new TodoItem(itemText);
        itemsAdapter.add(todoItem);
        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        launchEditView(position);
                    }
                }
        );
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            ArrayList<String> strItems = new ArrayList<String>(FileUtils.readLines(todoFile));
            ArrayList<TodoItem> result = new ArrayList<TodoItem>();
            for (String s : strItems) {
                TodoItem todoItem = new TodoItem(s);
                result.add(todoItem);
            }
            items = result;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            ArrayList<String> result = new ArrayList<String>();
            for (TodoItem item : items) {
                String s = item.title;
                result.add(s);
            }

            FileUtils.writeLines(todoFile, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final int REQUEST_CODE = 20;

    public void launchEditView(int position) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        String text = items.get(position).title;
        i.putExtra("text", text);
        i.putExtra("position", position);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String name = data.getExtras().getString("text");
            int position = data.getExtras().getInt("position", 0);
            TodoItem todoItem = new TodoItem(name);
            items.set(position, todoItem);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }
}
