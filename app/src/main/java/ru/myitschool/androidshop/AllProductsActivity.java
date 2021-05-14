package ru.myitschool.androidshop;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllProductsActivity extends ListActivity {

    ArrayList<HashMap<String, String>> productsList;
    JSONParser jParser = new JSONParser();
    JSONArray products = null;
    private static String url_all_products = "http://192.168.0.31/shop/get_all_products.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        productsList = new ArrayList<HashMap<String, String>>();

        new LoadAllProductsTask().execute();
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                Intent intentEdit = new Intent(getApplicationContext(), EditProductActivity.class);
                intentEdit.putExtra(TAG_PID, pid);
                startActivity(intentEdit);
                startActivityForResult(intentEdit, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Intent i = getIntent();
            finish();
            startActivity(i);
        }

    }

    private class LoadAllProductsTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        /*   pDialog = new ProgressDialog(AllProductsActivity.this);
            pDialog.setMessage("Загрузка списка. Подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
            Toast.makeText(getApplicationContext(),"Загрузка списка. Подождите...",Toast.LENGTH_LONG).show();
        }



        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        productsList.add(map);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            ListAdapter adapter = new SimpleAdapter(AllProductsActivity.this,
                    productsList,
                    R.layout.list_item, new String[]{TAG_PID, TAG_NAME}, new int[]{R.id.pid, R.id.name});
            setListAdapter(adapter);
        }

    }
}