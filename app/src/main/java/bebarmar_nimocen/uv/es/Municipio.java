package bebarmar_nimocen.uv.es;

import java.io.Serializable;

public class Municipio implements Serializable {
    private int id;
    private int codMun;
    private String mun;
    private int casosPCR;
    private String incidenciaPCR;
    private int casosPCR14;
    private String incidenciaPCR14;
    private int defunciones;
    private String tasaDefuncion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodMun() {
        return codMun;
    }

    public void setCodMun(int codMun) {
        this.codMun = codMun;
    }

    public String getMun() {
        return mun;
    }

    public void setMun(String mun) {
        this.mun = mun;
    }

    public int getCasosPCR() {
        return casosPCR;
    }

    public void setCasosPCR(int casosPCR) {
        this.casosPCR = casosPCR;
    }

    public String getIncidenciaPCR() {
        return incidenciaPCR;
    }

    public void setIncidenciaPCR(String incidenciaPCR) {
        this.incidenciaPCR = incidenciaPCR;
    }

    public int getCasosPCR14() {
        return casosPCR14;
    }

    public void setCasosPCR14(int casosPCR14) {
        this.casosPCR14 = casosPCR14;
    }

    public String getIncidenciaPCR14() {
        return incidenciaPCR14;
    }

    public void setIncidenciaPCR14(String incidenciaPCR14) {
        this.incidenciaPCR14 = incidenciaPCR14;
    }

    public int getDefunciones() {
        return defunciones;
    }

    public void setDefunciones(int defunciones) {
        this.defunciones = defunciones;
    }

    public String getTasaDefuncion() {
        return tasaDefuncion;
    }

    public void setTasaDefuncion(String tasaDefuncion) {
        this.tasaDefuncion = tasaDefuncion;
    }
}
