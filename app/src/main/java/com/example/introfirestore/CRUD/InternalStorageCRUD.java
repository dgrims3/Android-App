package com.example.introfirestore.CRUD;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class InternalStorageCRUD {

    public void updateFile(Context context, String fileName, String content){
        File path = context.getApplicationContext().getFilesDir();
        File file = new File(path, fileName);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content.toLowerCase()+"\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> readFile(Context context, String fileName){
        ArrayList<String> arrayList = new ArrayList<>();
        File path = context.getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(readFrom))) {
            String line = reader.readLine();
            while (line != null) {
                if(line.length()>1){
                    arrayList.add(line.toLowerCase());}
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public void deleteFromFile(Context context, String fileName, String itemToDelete){
        ArrayList<String> arrayList = new ArrayList<>();
        File path = context.getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        Scanner s = null;
        try {
            s = new Scanner(readFrom);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (Objects.requireNonNull(s).hasNext()){
            arrayList.add(s.next().toLowerCase());
        }
        s.close();
        arrayList.removeIf(i -> i.equals(itemToDelete.toLowerCase()));

        try {
            FileOutputStream writer = new FileOutputStream(readFrom);
            for (String i:arrayList
            ) {
                i = i+"\n";
                writer.write(i.getBytes());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
