package skylist.com.qrcodepresenca;

public class CheckDoDia {
    String institution, passOfDay, preceptor, date, timeCheckin, materia = null;

    public CheckDoDia(String institution, String passOfDay, String preceptor, String materia,String date, String timeCheckin) {
        this.institution = institution;
        this.passOfDay = passOfDay;
        this.preceptor = preceptor;
        this.date = date;
        this.timeCheckin = timeCheckin;
        this.materia = materia;
    }

    public CheckDoDia() {

    }

    @Override
    public String toString() {
        return "CheckDoDia{" +
                "institution='" + institution + '\'' +
                ", passOfDay='" + passOfDay + '\'' +
                ", preceptor='" + preceptor + '\'' +
                ", date='" + date + '\'' +
                ", timeCheckin='" + timeCheckin + '\'' +
                ", materia='" + materia + '\'' +
                '}';
    }
}
