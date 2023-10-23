package bebarmar_nimocen.uv.es;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

public class SQLController extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DiagnosticoBd.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DiagnosticContract.DiagnosticEntry.TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_MUNICIPIO + " TEXT," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_COD_DIAGNOSTICO + " TEXT," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_FECHA + " TEXT," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_FIEBRE + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_TOS + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_RESPIRAR + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_FATIGA + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_MUSCULAR + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_CABEZA + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_GUSTO_OLFATO + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_GARGANTA + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_CONGESTION + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_VOMITOS + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_DIARREA + " INTEGER," +
                    DiagnosticContract.DiagnosticEntry.COLUMN_CONTACTO + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DiagnosticContract.DiagnosticEntry.TABLE_NAME;

    public SQLController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private ContentValues addValues(Diagnostic diagnostic) {
        ContentValues values = new ContentValues();

        values.put(DiagnosticContract.DiagnosticEntry.COLUMN_MUNICIPIO, diagnostic.getMunicipio());
        values.put(DiagnosticContract.DiagnosticEntry.COLUMN_COD_DIAGNOSTICO, diagnostic.getCodigo());
        values.put(DiagnosticContract.DiagnosticEntry.COLUMN_FECHA, diagnostic.getDate());

        ArrayList<Integer> sintomas = diagnostic.getLista();

        for (int i = 0; i < sintomas.size(); i++) {
            int sintoma = sintomas.get(i);
            switch (i) {
                case 0:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_FIEBRE, sintoma);
                    break;
                case 1:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_TOS, sintoma);
                    break;
                case 2:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_RESPIRAR, sintoma);
                    break;
                case 3:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_FATIGA, sintoma);
                    break;
                case 4:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_MUSCULAR, sintoma);
                    break;
                case 5:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_CABEZA, sintoma);
                    break;
                case 6:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_GUSTO_OLFATO, sintoma);
                    break;
                case 7:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_GARGANTA, sintoma);
                    break;
                case 8:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_CONGESTION, sintoma);
                    break;
                case 9:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_VOMITOS, sintoma);
                    break;
                case 10:
                    values.put(DiagnosticContract.DiagnosticEntry.COLUMN_DIARREA, sintoma);
                    break;
            }
        }

        int contacto = 0;
        if (diagnostic.getContacto())
            contacto = 1;
        values.put(DiagnosticContract.DiagnosticEntry.COLUMN_CONTACTO, contacto);
        return values;
    }

    public void insertDiagnostic(Diagnostic diagnostic) {
        SQLiteDatabase bd = getWritableDatabase();

        ContentValues values = addValues(diagnostic);

        bd.insert(DiagnosticContract.DiagnosticEntry.TABLE_NAME, null, values);
        bd.close();
    }

    public void removeDiagnostic(Diagnostic diagnostic) {
        SQLiteDatabase bd = getWritableDatabase();
        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(diagnostic.getId())};
        bd.delete(DiagnosticContract.DiagnosticEntry.TABLE_NAME, selection, selectionArgs);

        bd.close();
    }

    public void updateDiagnostic(Diagnostic diagnostic) {
        SQLiteDatabase bd = getWritableDatabase();

        ContentValues values = addValues(diagnostic);

        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(diagnostic.getId())};

        bd.update(
                DiagnosticContract.DiagnosticEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public Cursor diagnosticsByMun(String municipio) {
        SQLiteDatabase bd = getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                DiagnosticContract.DiagnosticEntry.COLUMN_MUNICIPIO,
                DiagnosticContract.DiagnosticEntry.COLUMN_COD_DIAGNOSTICO,
                DiagnosticContract.DiagnosticEntry.COLUMN_FECHA,
                DiagnosticContract.DiagnosticEntry.COLUMN_FIEBRE,
                DiagnosticContract.DiagnosticEntry.COLUMN_TOS,
                DiagnosticContract.DiagnosticEntry.COLUMN_RESPIRAR,
                DiagnosticContract.DiagnosticEntry.COLUMN_FATIGA,
                DiagnosticContract.DiagnosticEntry.COLUMN_MUSCULAR,
                DiagnosticContract.DiagnosticEntry.COLUMN_CABEZA,
                DiagnosticContract.DiagnosticEntry.COLUMN_GUSTO_OLFATO,
                DiagnosticContract.DiagnosticEntry.COLUMN_GARGANTA,
                DiagnosticContract.DiagnosticEntry.COLUMN_CONGESTION,
                DiagnosticContract.DiagnosticEntry.COLUMN_VOMITOS,
                DiagnosticContract.DiagnosticEntry.COLUMN_DIARREA,
                DiagnosticContract.DiagnosticEntry.COLUMN_CONTACTO
        };

        String selection = DiagnosticContract.DiagnosticEntry.COLUMN_MUNICIPIO + " = ?";
        String[] selectionArgs = {municipio};

        return bd.query(
                DiagnosticContract.DiagnosticEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
}