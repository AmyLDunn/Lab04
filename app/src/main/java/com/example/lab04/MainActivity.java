package com.example.lab04;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView productID;
    EditText productName;
    EditText productPrice;
    ListView productList;
    ArrayList<String> listItems;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productID = (TextView) findViewById(R.id.product_id_text_view);
        productName = (EditText) findViewById(R.id.product_name_text_view);
        productPrice = (EditText) findViewById(R.id.product_price_text_view);
        productList = (ListView) findViewById(R.id.list_view);

        MyDBHandler database = new MyDBHandler(this);
        listItems = new ArrayList<>();

        viewData();

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = productList.getItemAtPosition(i).toString();
                Toast.makeText(MainActivity.this, ""+text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void newProduct(View view){
        MyDBHandler database = new MyDBHandler(this);
        double price = Double.parseDouble(productPrice.getText().toString());
        Product product = new Product(productName.getText().toString(), price);
        database.addProduct(product);
        productName.setText("");
        productPrice.setText("");
        listItems.clear();
        viewData();
    }

    public void lookupProduct(View view){
        MyDBHandler database = new MyDBHandler(this);
        Product product = database.findProduct(productName.getText().toString());
        if ( product != null ) {
            productID.setText(String.valueOf(product.getID()));
            productPrice.setText(String.valueOf(product.getPrice()));
        } else {
            productID.setText("No Match Found");
        }
    }

    public void removeProduct(View view){
        MyDBHandler database = new MyDBHandler(this);
        boolean result = database.deleteProduct(productName.getText().toString());
        listItems.clear();
        viewData();
        if (result){
            productID.setText("Record Deleted");
            productName.setText("");
            productPrice.setText("");
        } else {
            productID.setText("No Match Found");
        }
    }

    private void viewData(){
        MyDBHandler database = new MyDBHandler(this);
        Cursor cursor = database.viewData();
        if ( cursor.getCount() == 0){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                listItems.add(cursor.getString(1));
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
            productList.setAdapter(adapter);
        }
    }
}