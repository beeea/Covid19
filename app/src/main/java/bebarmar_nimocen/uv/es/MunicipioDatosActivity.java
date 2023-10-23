package bebarmar_nimocen.uv.es;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MunicipioDatosActivity extends AppCompatActivity {

    private AdapterDiagnostic adapter;
    private Municipio municipio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipio_datos);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_municipio);
        setSupportActionBar(toolbar);

        municipio = (Municipio) getIntent().getExtras().get("Municipio");

        fillMunicipioCard(municipio);

        //Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterDiagnostic(this);

        adapter.setCursor(getDiagnosticCursor(municipio.getMun()));
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent in = new Intent(getApplicationContext(), DiagnosticActivity.class);
            in.putExtra("Municipio", municipio.getMun());
            startActivity(in);
        });
    }

    private Cursor getDiagnosticCursor(String municipio) {
        SQLController sqlController = new SQLController(this);
        return sqlController.diagnosticsByMun(municipio);
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = getDiagnosticCursor(municipio.getMun());
        adapter.setCursor(cursor);
        adapter.notifyDataSetChanged();
    }

    private final View.OnClickListener onItemClickListener = view -> {
        DiagnosticViewHolder viewHolder = (DiagnosticViewHolder) view.getTag();

        Diagnostic diagnostic = viewHolder.getDiagnostic();

        Bundle bundle = new Bundle();
        bundle.putString("Municipio", municipio.getMun());
        bundle.putSerializable("Diagnostico", diagnostic);

        Intent in = new Intent(getApplicationContext(), DiagnosticActivity.class);
        in.putExtras(bundle);

        startActivity(in);
    };

    private void fillMunicipioCard(Municipio municipio) {
        TextView id = findViewById(R.id.id);
        TextView codigo = findViewById(R.id.codMunicipio);
        TextView mun = findViewById(R.id.municipioDatos);
        TextView muertes = findViewById(R.id.muertes);
        TextView incidencia = findViewById(R.id.incidencia);
        TextView casos = findViewById(R.id.casos);
        TextView casos14 = findViewById(R.id.casos14);
        TextView incidencia14 = findViewById(R.id.incidencia14);
        TextView tasa_muerte = findViewById(R.id.tasa);


        id.setText(String.valueOf(municipio.getId()));
        codigo.setText(String.valueOf(municipio.getCodMun()));
        mun.setText(municipio.getMun());
        muertes.setText(String.valueOf(municipio.getDefunciones()));
        incidencia.setText(municipio.getIncidenciaPCR());
        casos.setText(String.valueOf(municipio.getCasosPCR()));
        casos14.setText(String.valueOf(municipio.getCasosPCR14()));
        incidencia14.setText(municipio.getIncidenciaPCR14());
        tasa_muerte.setText(municipio.getTasaDefuncion());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.info_municipio, menu);
        return true;
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.boton_mapa) {// Do something when the user clicks on the button abc
            TextView municipality = findViewById(R.id.municipioDatos);
            Uri geoLocation = Uri.parse("geo:0,0?q=" + municipality.getText().toString().replace(" ", "+"));
            showMap(geoLocation);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}