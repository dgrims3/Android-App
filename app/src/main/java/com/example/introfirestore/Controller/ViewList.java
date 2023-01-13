package com.example.introfirestore.Controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.introfirestore.Adapter.RecyclerViewAdapter;
import com.example.introfirestore.CRUD.FirebaseCRUD;
import com.example.introfirestore.CRUD.InternalStorageCRUD;
import com.example.introfirestore.Interface.SelectListener;
import com.example.introfirestore.Model.FoodItem;
import com.example.introfirestore.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ViewList extends AppCompatActivity implements SelectListener {

    FirebaseCRUD firebaseCRUD;
    InternalStorageCRUD internalStorageCRUD;
    ArrayList<FoodItem> foodList;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    LinearLayout linearLayout;
    AlertDialog.Builder builder;
    String pathToFoodList = "tempfoodlist.txt";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);


        firebaseCRUD = new FirebaseCRUD();
        internalStorageCRUD = new InternalStorageCRUD();
        foodList = readFile(pathToFoodList);


        linearLayout = findViewById(R.id.linear_layout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, foodList, this);
        recyclerView.setAdapter(adapter);

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Delete Item?");


        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onItemClicked(FoodItem foodItem) {
        builder.setMessage("Delete "+foodItem.getName()+"?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Snackbar snackbar = Snackbar.make(linearLayout, "Item Deleted", Snackbar.LENGTH_SHORT);
                snackbar.show();
                firebaseCRUD.deleteFire(foodItem.getName());
                foodList.remove(foodItem);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            builder.setMessage("Delete "+foodList.get(viewHolder.getBindingAdapterPosition()).getName()+"?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Snackbar snackbar = Snackbar.make(linearLayout, "Item Deleted", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    firebaseCRUD.deleteFire((foodList.get(viewHolder.getBindingAdapterPosition()).getName()));
                    foodList.remove(viewHolder.getBindingAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyDataSetChanged();
                }
            });
            builder.show();
        }
    };

    public ArrayList<FoodItem> readFile(String fileName){
        ArrayList<FoodItem> arrayList = new ArrayList<>();
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(readFrom))) {
            String line = reader.readLine();
            while (line != null) {
                if(line.length()>1){
                arrayList.add(new FoodItem(line));
            }line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


}