package bebarmar_nimocen.uv.es;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AdapterMunicipios extends RecyclerView.Adapter<MunicipioViewHolder> implements Filterable {
    public ArrayList<Municipio> municipios;
    public ArrayList<Municipio> municipios_copia;
    Context context;
    private View.OnClickListener mOnItemClickListener;

    public AdapterMunicipios(Context c, ArrayList<Municipio> municipios) {
        context = c;
        this.municipios = municipios;
        Init();
    }

    public Municipio getMun(int pos) {
        return municipios.get(pos);
    }

    public void setMun(Municipio mun) {
        this.mun = mun;
    }

    private Municipio mun;

    public void Init(){
        municipios_copia = new ArrayList<>();
        municipios_copia.addAll(municipios);
    }

    /*public void Init() {
        // We read the JSON file and fill the “municipios” ArrayList
        municipios = new ArrayList<>();
        municipios_copia = new ArrayList<>();

        InputStream is = context.getResources().openRawResource(R.raw.covid);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n = reader.read(buffer);
            while (n != -1) {
                writer.write(buffer, 0, n);
                n = reader.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                municipios_copia.add(municipio);
            }
            Collections.sort(municipios, new IncidenciaComparator().reversed());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public int getItemCount() {
        return municipios.size();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MunicipioViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        // Put this line in the code of the onCreateViewHolder method
        view.setOnClickListener(mOnItemClickListener);

        return new MunicipioViewHolder(view);
    }

    public void onBindViewHolder(MunicipioViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        setMun(municipios.get(position));
        holder.setMunicipio(String.valueOf(municipios.get(position).getMun()).toUpperCase());
        holder.setIncidenciaPCR(String.valueOf(municipios.get(position).getIncidenciaPCR()));
        holder.setCasosPCR(String.valueOf(municipios.get(position).getCasosPCR()));
        holder.setMuertes(String.valueOf(municipios.get(position).getDefunciones()));
        colorIncidencia(holder);
    }

    public void colorIncidencia(MunicipioViewHolder holder) {
        TextView incidencias = holder.getIncidenciaPCRTextView();
        float inc = Float.parseFloat(holder.getIncidenciaPCR());

        float low = 10000.0f;
        float medium = 15000.0f;
        float high = 20000.0f;

        if (inc <= low) {
            incidencias.setTextColor(Color.parseColor("#96CF6C"));
        } else if (inc <= medium && inc < high) {
            incidencias.setTextColor(Color.parseColor("#ED9A2A"));
        } else {
            incidencias.setTextColor(Color.parseColor("#C53825"));
        }
    }

    public void botonAlfabeto() {
        Collections.sort(municipios, new MunicipioComparator());
    }

    public void botonAscendente() {
        Collections.sort(municipios, new DeathComparator());
    }

    public void botonDescendente() {
        Collections.sort(municipios, new DeathComparator().reversed());

    }

    public void botonIncidencia() {
        Collections.sort(municipios, new IncidenciaComparator().reversed());
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                ArrayList<Municipio> mun_filtered;

                if (charString.isEmpty()) {
                    mun_filtered = municipios_copia;
                } else {
                    ArrayList<Municipio> filteredList = new ArrayList<>();
                    for (Municipio row : municipios_copia) {
                        if (row.getMun().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    mun_filtered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mun_filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                municipios = (ArrayList<Municipio>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

class MunicipioComparator implements Comparator<Municipio> {

    @Override
    public int compare(Municipio municipio1, Municipio municipio2) {
        return municipio1.getMun().compareTo(municipio2.getMun());
    }

    @Override
    public Comparator<Municipio> reversed() {
        return Comparator.super.reversed();
    }
}

class DeathComparator implements Comparator<Municipio> {

    @Override
    public int compare(Municipio municipio1, Municipio municipio2) {
        return Integer.compare(municipio1.getDefunciones(), municipio2.getDefunciones());
    }

    @Override
    public Comparator<Municipio> reversed() {
        return Comparator.super.reversed();
    }
}

class IncidenciaComparator implements Comparator<Municipio> {

    @Override
    public int compare(Municipio municipio1, Municipio municipio2) {
        float inc1 = Float.parseFloat(municipio1.getIncidenciaPCR());
        float inc2 = Float.parseFloat(municipio2.getIncidenciaPCR());

        return Float.compare(inc1, inc2);
    }

    @Override
    public Comparator<Municipio> reversed() {
        return Comparator.super.reversed();
    }
}