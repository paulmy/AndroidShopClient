package ru.myitschool.androidshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnViewProducts;
    Button btnNewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        btnNewProduct = findViewById(R.id.btnCreateProduct);
        btnViewProducts.setOnClickListener((View view) -> {
            Intent intent =
                    new Intent(getApplicationContext(), AllProductsActivity.class);
            startActivity(intent);
        });
        btnNewProduct.setOnClickListener(view -> {
            Intent intent =
                    new Intent(getApplicationContext(), NewProductActivity.class);
            startActivity(intent);
        });


    }


}