package bebarmar_nimocen.uv.es;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiagnosticViewHolder extends RecyclerView.ViewHolder {
    private final TextView code;
    private final TextView fecha;
    private final TextView sintomas;
    private final TextView contacto;
    public Diagnostic diagnostic;

    String[] simArray = {"Fiebre o escalofríos", "Tos", "Falta de aire o dificultad para respirar", "Fatiga",
            "Dolores musculares o corporales", "Dolor de cabeza", "Nueva pérdida del gusto o del olfato",
            "Dolor de garganta", "Congestión o secreción nasal", "Náuseas o vómitos", "Diarrea"};

    public DiagnosticViewHolder(View view) {
        super(view);

        code = view.findViewById(R.id.code);
        fecha = view.findViewById(R.id.fecha);
        sintomas = view.findViewById(R.id.sintomas);
        contacto = view.findViewById(R.id.contacto);

        // Put this line in the code of the ViewHolder constructor
        view.setTag(this);
    }

    public void setCode(String code) {
        this.code.setText(code);
    }

    public void setSintomas(ArrayList<Integer> sintomas) {
        StringBuilder sintomasText = new StringBuilder();

        for (int i = 0; i < sintomas.size(); i++) {
            if (sintomas.get(i) != 0)
                sintomasText.append(simArray[i]).append(",");
        }

        if (!sintomas.isEmpty() && sintomasText.lastIndexOf(",") != -1 && sintomasText.lastIndexOf(",") == sintomas.size() - 1)
            sintomasText.deleteCharAt(sintomasText.length() - 1);

        this.sintomas.setText(sintomasText);
    }

    public void setContacto(int contacto) {
        if (contacto == 0)
            this.contacto.setText("No");
        else
            this.contacto.setText("Sí");
    }

    public void setFecha(String fecha) {
        this.fecha.setText(fecha);
    }

    public void setDiagnostic(Diagnostic diagnostico) {
        diagnostic = diagnostico;
    }

    public Diagnostic getDiagnostic() {
        return diagnostic;
    }
}