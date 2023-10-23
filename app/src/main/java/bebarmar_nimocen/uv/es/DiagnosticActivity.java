package bebarmar_nimocen.uv.es;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class DiagnosticActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    boolean contactoDirecto = true;
    boolean[] selectedSintomas;
    String[] simArray = {"Fiebre o escalofríos", "Tos", "Falta de aire o dificultad para respirar", "Fatiga",
            "Dolores musculares o corporales", "Dolor de cabeza", "Nueva pérdida del gusto o del olfato",
            "Dolor de garganta", "Congestión o secreción nasal", "Náuseas o vómitos", "Diarrea"};
    private SQLController sqlController;
    Diagnostic diagnostic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_management);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedSintomas = new boolean[simArray.length];

        Bundle extras = getIntent().getExtras();

        setUpSymptomsMenu();
        SpinnerContacto();

        if (extras != null) {
            String municipio = (String) extras.get("Municipio");
            EditText editMun = findViewById(R.id.municipio_id);
            editMun.setText(municipio);

            diagnostic = (Diagnostic) extras.get("Diagnostico");

            if (diagnostic != null) {
                EditText editCodigo = findViewById(R.id.codigo_diagnostico);
                editCodigo.setText(diagnostic.getCodigo());

                EditText editFecha = findViewById(R.id.editFecha);
                editFecha.setText(diagnostic.getDate());

                if (diagnostic.getContacto())
                    SpinnerContacto().setSelection(0);
                else
                    SpinnerContacto().setSelection(1);

                TextView textView = findViewById(R.id.sintomas);
                ArrayList<Integer> sintomas = diagnostic.getLista();
                StringBuilder sintomasText = new StringBuilder();

                for (int i = 0; i < sintomas.size(); i++) {
                    selectedSintomas[i] = sintomas.get(i) == 1;
                    if (selectedSintomas[i])
                        sintomasText.append(simArray[i]).append(", ");
                }

                if (sintomasText.length() != 0 && sintomasText.lastIndexOf(",") != -1 && sintomasText.lastIndexOf(",") == sintomasText.length() - 1)
                    sintomasText.deleteCharAt(sintomasText.length() - 1);
                textView.setText(sintomasText.toString());
            }
        }

        sqlController = new SQLController(this);
    }

    public void onItemSelected(@NonNull AdapterView<?> parent, View view, int pos, long id) {
        String contacto = (String) parent.getItemAtPosition(pos);
        contactoDirecto = contacto.equals("Sí");
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void setUpSymptomsMenu() {
        TextView textView = findViewById(R.id.sintomas);

        textView.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DiagnosticActivity.this);
            builder.setTitle("Selecciona tus síntomas");
            builder.setCancelable(false);

            builder.setMultiChoiceItems(simArray, selectedSintomas, (dialogInterface, index, isChecked) -> {
                selectedSintomas[index] = isChecked;
            });

            builder.setPositiveButton("Aceptar", (dialogInterface, index) -> {
                StringBuilder sintomasText = new StringBuilder();
                for (int i = 0; i < selectedSintomas.length; i++) {
                    if (selectedSintomas[i])
                        sintomasText.append(simArray[i]).append(", ");
                }

                if (sintomasText.length() != 0 && sintomasText.lastIndexOf(",") != -1 && sintomasText.lastIndexOf(",") == sintomasText.length() - 1)
                    sintomasText.deleteCharAt(sintomasText.length() - 1);
                textView.setText(sintomasText.toString());
            });

            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.setNeutralButton("Borrar", (dialogInterface, i) -> {
                for (int j = 0; j < selectedSintomas.length; j++) {
                    selectedSintomas[j] = false;
                    textView.setText("");
                }
            });
            builder.show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_diagnostico, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Diagnostic diagnostico = getDiagnostic();

        switch (item.getItemId()) {
            case R.id.guardar:
                if (diagnostic == null) {
                    if (comprobarDatos(diagnostico)) {
                        sqlController.insertDiagnostic(diagnostico);
                        onBackPressed();
                    } else {
                        android.app.AlertDialog.Builder guardarAlerta = new android.app.AlertDialog.Builder(this);
                        guardarAlerta.setTitle("¡Faltan datos!");
                        guardarAlerta.setMessage("Rellena los datos que están vacíos");
                        guardarAlerta.setPositiveButton("Aceptar", (dialog, id) -> dialog.dismiss());
                        guardarAlerta.show();
                    }
                } else {
                    sqlController.updateDiagnostic(diagnostico);
                    onBackPressed();
                }
                return true;

            case R.id.borrar:
                sqlController.removeDiagnostic(diagnostico);
                onBackPressed();
                return true;

            case R.id.ubi:
                setMunByUbicacion();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMunByUbicacion(){
        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(
                this,
                permisos, 1
        );
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                if (myLocation != null) {
                    Address address = geocoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1).get(0);
                    if (address != null) {
                        TextView munText = findViewById(R.id.municipio_id);
                        munText.setText(address.getLocality());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Diagnostic getDiagnostic() {
        TextView codigoText = findViewById(R.id.codigo_diagnostico);
        String codigo = codigoText.getText().toString();

        TextView dateText = findViewById(R.id.editFecha);
        String date = dateText.getText().toString();

        TextView munText = findViewById(R.id.municipio_id);
        String munDiagnostico = munText.getText().toString();

        int id = 0;
        if (diagnostic != null) {
            id = diagnostic.getId();
        }

        ArrayList<Integer> simList = new ArrayList<>();
        for (boolean selectedSintoma : selectedSintomas) {
            if (selectedSintoma)
                simList.add(1);
            else
                simList.add(0);
        }

        return new Diagnostic(id, codigo, date, simList, contactoDirecto, munDiagnostico);
    }

    private Boolean comprobarDatos(Diagnostic diagnostic) {
        return !diagnostic.getCodigo().isEmpty() && !diagnostic.getDate().isEmpty() && !diagnostic.getMunicipio().isEmpty();
    }

    public Spinner SpinnerContacto() {
        Spinner spinner = (Spinner) findViewById(R.id.contactoDirecto);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.contactoDirecto,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return spinner;
    }
}