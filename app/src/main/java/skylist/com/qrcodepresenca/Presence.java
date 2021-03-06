package skylist.com.qrcodepresenca;

public class Presence {
    private String materia      = null;
    private String day          = null;
    private String preceptor    = null;
    private String passOfDay     = null;

    Presence( String materia, String day, String preceptor ){
        this.materia = materia;
        this.day = day;
        this.preceptor = preceptor;
    }
    Presence( String materia, String day, String preceptor, String passOfDay ){
        this.materia = materia;
        this.day = day;
        this.preceptor = preceptor;
        this.passOfDay = passOfDay;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPreceptor() {
        return preceptor;
    }

    public void setPreceptor(String preceptor) {
        this.preceptor = preceptor;
    }

    public String getPassOfDay() {
        return passOfDay;
    }

    public void setPassOfDay(String passOfDay) {
        this.passOfDay = passOfDay;
    }
}
