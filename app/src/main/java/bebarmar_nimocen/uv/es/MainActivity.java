package bebarmar_nimocen.uv.es;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private AdapterMunicipios adapter;
    private String databaseID = "7fd9a2bf-ffee-4604-907e-643a8009b04e";

    private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            Municipio municipio = adapter.getMun(viewHolder.getAdapterPosition());
            // Implement the listener!
            Intent in = new Intent(getApplicationContext(), MunicipioDatosActivity.class).putExtra(
                    "Municipio",
                    municipio
            );
            startActivity(in);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the RecyclerView
        /*RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterMunicipios(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);

        adapter = (AdapterMunicipios) recyclerView.getAdapter();*/
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> {
            Uri link = Uri.parse("https://dadesobertes.gva.es/es/dataset/covid-19-casos-confirmats-pcr-casos-pcr-en-els-ultims-14-dies-i-persones-mortes-per-municipi-2022");
            Intent i = new Intent(Intent.ACTION_VIEW, link);
            startActivity(i);
        });

        //Boton añadir diagnostico
        FloatingActionButton fab = findViewById(R.id.addReport);
        fab.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), DiagnosticActivity.class);
            startActivity(in);
        });

        HTTPConnector httpConnector = new HTTPConnector();
        httpConnector.execute();

        //Boton refrescar datos
        FloatingActionButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            HTTPDatabaseConnector httpDatabaseConnector = new HTTPDatabaseConnector();
            httpDatabaseConnector.execute();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        AppCompatImageView search_icon = searchView.findViewById(androidx.appcompat.R.id.search_button);
        search_icon.setImageResource(R.drawable.ic_search);

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.boton_abc:
                // Do something when the user clicks on the button abc
                if (adapter != null) {
                    adapter.botonAlfabeto();
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.boton_down:
                // Do something when the user clicks on the button down
                if (adapter != null) {
                    adapter.botonDescendente();
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.boton_up:
                // Do something when the user clicks on the button up
                if (adapter != null) {
                    adapter.botonAscendente();
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.boton_incidencia:
                // Do something when the user clicks on the button incidencia
                if (adapter != null) {
                    adapter.botonIncidencia();
                    adapter.notifyDataSetChanged();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class HTTPConnector extends AsyncTask<String, Void, ArrayList<Municipio>> {
        @Override
        protected ArrayList<Municipio> doInBackground(String... params) {
            ArrayList<Municipio> municipios = new ArrayList<>();
            String url = "https://dadesobertes.gva.es/es/api/3/action/datastore_search?resource_id=" + databaseID + "&limit=1000";
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json;");
                con.setRequestProperty("accept-language", "es");
                con.connect();
                int responseCode = con.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
                int n;
                while ((n = in.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                in.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //The String writer.toString() must be parsed in the municipalities ArrayList by using JSONArray and JSONObject
            try {
                JSONObject obj = new JSONObject(String.valueOf(writer));
                JSONObject result = obj.getJSONObject("result");
                JSONArray records = result.getJSONArray("records");

                for (int i = 0; i < records.length(); i++) {
                    JSONObject object = records.getJSONObject(i);
                    Municipio municipio = new Municipio();
                    municipio.setId(object.getInt("_id"));
                    municipio.setCodMun(object.getInt("CodMunicipio"));
                    municipio.setMun(object.getString("Municipi"));
                    municipio.setCasosPCR(object.getInt("Casos PCR+"));
                    municipio.setIncidenciaPCR(object.getString("Incidència acumulada PCR+").trim().replace(",", "."));
                    municipio.setCasosPCR14(object.getInt("Casos PCR+ 14 dies"));
                    municipio.setIncidenciaPCR14(object.getString("Incidència acumulada PCR+14").trim().replace(",", "."));
                    municipio.setDefunciones(object.getInt("Defuncions"));
                    municipio.setTasaDefuncion((object.getString("Taxa de defunció").trim().replace(",", ".")));
                    municipios.add(municipio);
                }
                Collections.sort(municipios, new IncidenciaComparator().reversed());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return municipios;
        }

        @Override
        protected void onPostExecute(ArrayList<Municipio> municipios) {
            // Create the RecyclerView
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter = new AdapterMunicipios(MainActivity.this, municipios);
            adapter.setOnItemClickListener(onItemClickListener);
            recyclerView.setAdapter(adapter);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class HTTPDatabaseConnector extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = "https://dadesobertes.gva.es/api/3/action/package_show?id=38e6d3ac-fd77-413e-be72-aed7fa6f13c2";
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            try {
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("GET");
                //add request header
                con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
                con.setRequestProperty("accept", "application/json;");
                con.setRequestProperty("accept-language", "es");
                con.connect();
                int responseCode = con.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
                int n;
                while ((n = in.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
                in.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String newDatabaseID = "";

            try {
                JSONObject obj = new JSONObject(String.valueOf(writer));
                JSONObject result = obj.getJSONObject("result");
                JSONArray resources = result.getJSONArray("resources");
                newDatabaseID = resources.getJSONObject(0).getString("id");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newDatabaseID;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != databaseID) {
                databaseID = result;
                HTTPConnector httpConnector = new HTTPConnector();
                httpConnector.execute();
            }
        }
    }
}