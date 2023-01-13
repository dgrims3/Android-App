package com.example.introfirestore.Controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.introfirestore.CRUD.FirebaseCRUD;
import com.example.introfirestore.CRUD.InternalStorageCRUD;
import com.example.introfirestore.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    FirebaseCRUD firebaseCRUD;
    InternalStorageCRUD internalStorageCRUD;
    ListView listView;
    ArrayAdapter arrayAdapter;
    SearchView searchView;
    Button viewListButton;
    String pathToFoodList = "food.txt";
    String pathToTempFoodList = "tempfoodlist.txt";
    ArrayList<String> foodList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("FoodList")
            .document("List");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseCRUD = new FirebaseCRUD();
        internalStorageCRUD = new InternalStorageCRUD();

        clearAndWrite(this);

        viewListButton = findViewById(R.id.view_list_button);
        listView = findViewById(R.id.list_view);
        searchView = findViewById(R.id.search_bar);
        foodList = internalStorageCRUD.readFile(this, pathToFoodList);
        Collections.sort(foodList);
        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, foodList);


        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmAddItem(searchView, searchView.getQuery().toString()+"\n");
            }
        });
        listView.addFooterView(footerView);
        listView.setAdapter(arrayAdapter);
        listView.setVisibility(View.INVISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                confirmAddItem(searchView, selectedItem);
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                searchView.setIconified(false);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listView.setVisibility(View.INVISIBLE);
                searchView.onActionViewCollapsed();
                searchView.setIconified(false);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                listView.setVisibility(View.VISIBLE);
                searchView.setIconified(false);
                return false;
            }

        });


        viewListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewList.class);
                startActivity(intent);

            }
        });

    }

    public void confirmAddItem(SearchView searchView, String item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Add "+item+" to Shopping List?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!foodList.contains(item)){
                            internalStorageCRUD.updateFile(MainActivity.this, pathToFoodList, item);
                        }
                        firebaseCRUD.updateFire(item.toLowerCase().trim());
                        internalStorageCRUD.updateFile(MainActivity.this, pathToTempFoodList, item);
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void clearAndWrite(Context context){
        File path = context.getApplicationContext().getFilesDir();
        File file = new File(path, "tempfoodlist.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            writer.close();
            } catch (IOException e) {
            e.printStackTrace();
            }finally {
            firebaseCRUD.readFire(MainActivity.this);
        }
    }

}

