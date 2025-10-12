//@ian

package model;

public class Member {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String memberSince;

    public Member(String id, String name, String email, String phone, String memberSince) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.memberSince = memberSince;
    }

    public String toCsv() {
        return String.join(",", id, name, email, phone, memberSince);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getMemberSince() { return memberSince; }
}
