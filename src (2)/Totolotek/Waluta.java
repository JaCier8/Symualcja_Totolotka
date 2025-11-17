package Totolotek;

public class Waluta {
    private long grosze;

    public Waluta(long x) {
        this.grosze = x;
    }

    public Waluta(){
        this.grosze = 0L;
    }

    public Waluta(Waluta waluta) {
        this.grosze = waluta.grosze();
    }

    public boolean czyNieUjemna() {
        return grosze > 0L;
    }

    public void dodaj(Waluta waluta){
        grosze += waluta.grosze();
    }
    public Waluta suma(Waluta waluta){
        return new Waluta(grosze + waluta.grosze());
    }

    public void odejmij(Waluta waluta){
        grosze -= waluta.grosze();
    }
    public Waluta roznica(Waluta waluta){
        return new Waluta(grosze - waluta.grosze());
    }

    public void dodaj(long x){
        grosze += x;
    }
    public Waluta suma(long x){
        return new Waluta(grosze + x);
    }

    public void odejmij(int x){
        grosze -= x;
    }
    public Waluta roznica(long x){
        return new Waluta(grosze - x);
    }

    public void przemnoz(double x){
        grosze  = (long)(grosze * x);
    }
    public Waluta iloczyn(double x){
        return new Waluta((long)(grosze * x));
    }

    public void podziel(double x){
        if (x == 0) {
            return;
        }
        grosze  = (long)(grosze / x);
    }

    public Waluta iloraz(double x){
        if (x == 0) return new Waluta(this);
        return new Waluta((long)(grosze / x));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.grosze()/100);
        sb.append(" zł ");
        if (this.grosze()%100 < 10) sb.append("0");
        sb.append(this.grosze()%100);
        sb.append(" gr");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj.toString().equals(this.toString());
    }

    public long grosze() {
        return grosze;
    }


}
