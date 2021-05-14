package ru.myitschool.androidshop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivity extends Activity {
    EditText inputName;
    EditText inputPrice;
    EditText inputDesc;
    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static String url_create_product = "http://192.168.0.31/create_product.php";
    private static final String TAG_SUCCESS = "success";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        inputName = findViewById(R.id.inputName);
        inputPrice = findViewById(R.id.inputPrice);
        inputDesc = findViewById(R.id.inputDesc);
        Button btnCreateProduct = findViewById(R.id.btnCreateProduct);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateNewProductTask().execute();
            }
        });
    }

    private class CreateNewProductTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProductActivity.this);
            pDialog.setMessage("Create new product...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String name = inputName.getText().toString();
            String price = inputPrice.getText().toString();
            String description = inputDesc.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("price", price));
            params.add(new BasicNameValuePair("description", description));

            JSONObject jsonObject = jsonParser.makeHttpRequest(
                    url_create_product,
                    "POST",
                    params);
            try {
                int success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    finish();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Продукт не создан",
                            Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
        }
    }
}
