package com.deviyan.crudsqlite.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.deviyan.crudsqlite.database.DatabaseHelper;
import com.deviyan.crudsqlite.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditProductActivity extends AppCompatActivity {


    MaterialToolbar materialToolbar;
    MaterialButton mbtnUpdate, mbtnCancel;

    private EditText etName;
    private EditText etPrice;
    private EditText etUnit;
    private EditText etExpiration;
    private EditText etAvailInventory;
    private EditText etAvailInventoryCost;
    private ImageView ivProduct, ivDate;

    private static final int CAMERA_REQ_CODE = 100;
    private static final int STORAGE_REQ_CODE = 200;

    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    private String id, name, unit, price, expiration, availInventory, availCost, nDate;
    private DatabaseHelper databaseHelper;

    private DatePickerDialog datePickerDialog;

    private boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        materialToolbar = (MaterialToolbar) findViewById(R.id.mtb_main);
        mbtnUpdate = (MaterialButton) findViewById(R.id.mbtn_update);
        mbtnCancel = (MaterialButton) findViewById(R.id.mbtn_cancel);

        etName = (EditText) findViewById(R.id.et_name);
        etPrice = (EditText) findViewById(R.id.et_price);
        etUnit = (EditText) findViewById(R.id.et_unit);
        etExpiration = (EditText) findViewById(R.id.et_expiration);
        etAvailInventory = (EditText) findViewById(R.id.et_avail_inventory);
        ivProduct = (ImageView) findViewById(R.id.iv_add_photo);
        ivDate = (ImageView) findViewById(R.id.iv_date);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        databaseHelper = new DatabaseHelper(this);


        setSupportActionBar(materialToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", editMode);
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        unit = intent.getStringExtra("unit");
        price = intent.getStringExtra("price");
        expiration = intent.getStringExtra("date");
        availInventory = intent.getStringExtra("inv");
        availCost = intent.getStringExtra("cost");
        imageUri = Uri.parse(intent.getStringExtra("imagePath"));

        if(editMode){

            materialToolbar.setTitle("Update Product");
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            unit = intent.getStringExtra("unit");
            price = intent.getStringExtra("price");
            expiration = intent.getStringExtra("date");
            availInventory = intent.getStringExtra("inv");
            availCost = intent.getStringExtra("cost");
            imageUri = Uri.parse(intent.getStringExtra("imagePath"));

            etName.setText(name);
            etUnit.setText(unit);
            etPrice.setText(price);
            etExpiration.setText(expiration);
            etAvailInventory.setText(availInventory);

            if(imageUri.toString().equals("null")){
                ivProduct.setImageResource(R.drawable.ic_add_photo);
            }else{
                ivProduct.setImageURI(imageUri);
            }
        }else {
            materialToolbar.setTitle("Add Information");
        }


        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(EditProductActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // set day of month , month and year value in the edit text
//                                etExpiration.setText(dayOfMonth + "/"
//                                        + (monthOfYear + 1) + "/" + year);


                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);


                                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy");
                                nDate = format.format(calendar.getTime());
                                etExpiration.setText(nDate);

                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        ivProduct.setOnClickListener(view -> {
            imagePickDialog();
        });

        mbtnCancel.setOnClickListener(view -> {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        });


        mbtnUpdate.setOnClickListener(view -> {
            updateProduct();
        });

    }

    private void imagePickDialog() {

        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select");
        builder.setItems(options, (dialogInterface, i) -> {
            if(i == 0){
                //if 0, open the camera and check permission of camera
                if(!checkCameraPermission()){
                    requestCameraPermissions();
                }
                else{
                    pickFromCamera();
                }
            }
            else if (i == 1) {
                if(!checkStoragePermission()){
                    requestStoragePermission();
                }
                else  {
                    pickFromStorage();
                }
            }
        });
        builder.create().show();
    }

    private void pickFromStorage() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void updateProduct() {

        name = etName.getText().toString().trim();
        unit = etUnit.getText().toString().trim();
        price = etPrice.getText().toString().trim();
        expiration = etExpiration.getText().toString().trim();
        availInventory = etAvailInventory.getText().toString().trim();

        if(name.isEmpty() | unit.isEmpty() | price.isEmpty() | expiration.isEmpty() | availInventory.isEmpty() | imageUri == null ){
            Toast.makeText(EditProductActivity.this, "Please input all fields", Toast.LENGTH_LONG).show();
        }
        else {

            double nPrice = Double.parseDouble(price);
            double nAvailInv = Double.parseDouble(availInventory);
            double result = nAvailInv * nPrice;

            String nName = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
            String nUnit = unit.toLowerCase();

            if(editMode){
                databaseHelper.updateProduct(
                        ""+ id,
                        "" + nName,
                        "" + nUnit,
                        "" + price,
                        "" + expiration,
                        ""+ availInventory,
                        "" + result,
                        "" + imageUri
                );

                Toast.makeText(EditProductActivity.this, "Product successfully update", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditProductActivity.this, MainActivity.class));
            }
            else {

                databaseHelper.insertProduct(
                        "" + nName,
                        "" + nUnit,
                        "" + price,
                        "" + expiration,
                        ""+ availInventory,
                        "" + result,
                        "" + imageUri
                );

                Toast.makeText(EditProductActivity.this, "Product successfully added", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditProductActivity.this, MainActivity.class));

            }
        }

    }


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQ_CODE);
    }

    private boolean checkCameraPermission(){

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermissions(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case CAMERA_REQ_CODE: {
                if(grantResults.length > 0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "Camera permoission required", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            case STORAGE_REQ_CODE: {
                if(grantResults.length > 0){

                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted){
                        pickFromStorage();
                    }
                    else{
                        Toast.makeText(this, "Storage permission are necessary", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //this method be called after picking image from gallery
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked from gallery,
                //get uri of image
                imageUri = data.getData();

                //set to imageview
                ivProduct.setImageURI(imageUri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image is picked from camera
                //get uri of image
                ivProduct.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}