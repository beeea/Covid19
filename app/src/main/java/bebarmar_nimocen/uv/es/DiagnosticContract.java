package bebarmar_nimocen.uv.es;

import android.provider.BaseColumns;

public final class DiagnosticContract {
    private DiagnosticContract() {}

    public static class DiagnosticEntry implements BaseColumns {
        public static final String TABLE_NAME = "diagnostico";
        public static final String COLUMN_MUNICIPIO = "municipio";
        public static final String COLUMN_COD_DIAGNOSTICO = "codigoDiagnostico";
        public static final String COLUMN_FECHA = "fecha";
        public static final String COLUMN_FIEBRE = "fiebre";
        public static final String COLUMN_TOS = "tos";
        public static final String COLUMN_RESPIRAR = "respirar";
        public static final String COLUMN_FATIGA = "fatiga";
        public static final String COLUMN_MUSCULAR = "muscular";
        public static final String COLUMN_CABEZA = "cabeza";
        public static final String COLUMN_GUSTO_OLFATO = "gustoOlfato";
        public static final String COLUMN_GARGANTA = "garganta";
        public static final String COLUMN_CONGESTION = "congestion";
        public static final String COLUMN_VOMITOS = "vomitos";
        public static final String COLUMN_DIARREA = "diarrea";
        public static final String COLUMN_CONTACTO = "contacto";
    }
}