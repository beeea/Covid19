package bebarmar_nimocen.uv.es;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterDiagnostic extends RecyclerView.Adapter<DiagnosticViewHolder> {

    Context context;
    private View.OnClickListener mOnItemClickListener;
    private Cursor cursor;

    public AdapterDiagnostic(Context c) {
        context = c;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public DiagnosticViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_diagnostico, viewGroup, false);

        // Put this line in the code of the onCreateViewHolder method
        view.setOnClickListener(mOnItemClickListener);

        return new DiagnosticViewHolder(view);
    }

    public void onBindViewHolder(@NonNull DiagnosticViewHolder holder, int position) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (position >= 0 && position < getItemCount()) {
            cursor.moveToPosition(position);
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            String code = cursor.getString(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_COD_DIAGNOSTICO));
            String municipio = cursor.getString(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_MUNICIPIO));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_FECHA));
            int contacto = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_CONTACTO));
            ArrayList<Integer> sintomas = getSintomas(cursor);

            Diagnostic diagnostic = new Diagnostic(id, code, fecha, sintomas, contacto == 1, municipio);
            holder.setDiagnostic(diagnostic);

            holder.setCode(context.getResources().getString(R.string.diagnostic, code));
            holder.setFecha(fecha);
            holder.setContacto(contacto);
            holder.setSintomas(sintomas);
        }
    }

    private ArrayList<Integer> getSintomas(Cursor cursor) {
        ArrayList<Integer> sintomas = new ArrayList<>();
        int fiebre = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_FIEBRE));
        sintomas.add(fiebre);
        int tos = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_TOS));
        sintomas.add(tos);
        int respirar = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_RESPIRAR));
        sintomas.add(respirar);
        int fatiga = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_FATIGA));
        sintomas.add(fatiga);
        int muscular = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_MUSCULAR));
        sintomas.add(muscular);
        int cabeza = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_CABEZA));
        sintomas.add(cabeza);
        int olfato = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_GUSTO_OLFATO));
        sintomas.add(olfato);
        int garganta = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_GARGANTA));
        sintomas.add(garganta);
        int congestion = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_CONGESTION));
        sintomas.add(congestion);
        int vomitos = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_VOMITOS));
        sintomas.add(vomitos);
        int diarrea = cursor.getInt(cursor.getColumnIndexOrThrow(DiagnosticContract.DiagnosticEntry.COLUMN_DIARREA));
        sintomas.add(diarrea);
        return sintomas;
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }
}
