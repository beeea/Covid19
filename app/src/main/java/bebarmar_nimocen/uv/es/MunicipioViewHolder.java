package bebarmar_nimocen.uv.es;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder).
 */
public class MunicipioViewHolder extends RecyclerView.ViewHolder {
    private TextView municipio;
    private TextView muertes;
    private TextView incidencia;
    private TextView casos;

    public MunicipioViewHolder(View view) {
        super(view);

        municipio = view.findViewById(R.id.municipio);
        muertes = view.findViewById(R.id.muertes);
        incidencia = view.findViewById(R.id.incidencia);
        casos = view.findViewById(R.id.casos);

        // Put this line in the code of the ViewHolder constructor
        view.setTag(this);
    }

    public void setMunicipio(String name) {
        municipio.setText(name);
    }

    public void setIncidenciaPCR(String name) {
        incidencia.setText(name);
    }

    public void setCasosPCR(String name) {
        casos.setText(name);
    }

    public void setMuertes(String name) {
        muertes.setText(name);
    }

    public String getIncidenciaPCR() {
        return incidencia.getText().toString();
    }

    public TextView getIncidenciaPCRTextView() {
        return incidencia;
    }
}