package bebarmar_nimocen.uv.es;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Diagnostic implements Serializable {
    private int id;
    private String codigo;
    private String date;

    private ArrayList<Integer> lista;

    private boolean contacto_directo;

    private String municipio;

    public Diagnostic(int id, String codigo, String date, ArrayList<Integer> lista, boolean contacto_directo, String municipio) {
        this.id = id;
        this.codigo = codigo;
        this.date = date;
        this.lista = lista;
        this.contacto_directo = contacto_directo;
        this.municipio = municipio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Integer> getLista() {
        return lista;
    }

    public void setLista(ArrayList<Integer> lista) {
        this.lista = lista;
    }

    public boolean getContacto() {
        return contacto_directo;
    }

    public void setContacto(boolean contacto_directo) {
        this.contacto_directo = contacto_directo;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
