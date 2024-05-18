package Domain;

public class User implements Entity<Integer>{
    private int id;
    private String nume;
    private String ip;

    public User() {}

    public User(String nume, String ip) {
        this.nume = nume;
        this.ip = ip;
    }

    public User(int id, String nume, String ip) {
        this.id = id;
        this.nume = nume;
        this.ip = ip;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
