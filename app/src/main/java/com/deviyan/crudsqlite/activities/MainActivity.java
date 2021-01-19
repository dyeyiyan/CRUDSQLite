package com.deviyan.crudsqlite.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.deviyan.crudsqlite.database.Constants;
import com.deviyan.crudsqlite.database.DatabaseHelper;
import com.deviyan.crudsqlite.R;
import com.deviyan.crudsqlite.adapters.ProductAdapter;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar materialToolbar;
    RecyclerView rvProduct;
    DatabaseHelper databaseHelper;
    ProductAdapter productAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialToolbar = (MaterialToolbar) findViewById(R.id.mtb_main);
        rvProduct = (RecyclerView) findViewById(R.id.rv_product);
        databaseHelper = new DatabaseHelper(this);

        setSupportActionBar(materialToolbar);

        loadAllProduct();

    }

    private void loadAllProduct() {
        productAdapter = new ProductAdapter(getApplicationContext(), databaseHelper.getAll(Constants.C_ID + " DESC"));
        productAdapter.notifyDataSetChanged();
        rvProduct.setAdapter(productAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.add:
                //Toast.makeText(MainActivity.this, "Add is Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllProduct();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_BACK){
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

}