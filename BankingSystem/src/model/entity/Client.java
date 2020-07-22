package model.entity;

public class Client {
    private Long id;
    private String firstName;
    private String name;
    private String identityCardNumber;
    private String cnp;
    private String address;

    public Client(String firstName, String name, String identityCardNumber, String cnp, String address) {
        this.firstName = firstName;
        this.name = name;
        this.identityCardNumber = identityCardNumber;
        this.cnp = cnp;
        this.address = address;
    }

    public Client() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", name='" + name + '\'' +
                ", identityCardNumber='" + identityCardNumber + '\'' +
                ", cnp='" + cnp + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
