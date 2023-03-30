package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long user_id  ;
    private String login ;
    private String password  ;
    private String email ;
    private String phone ;
    private String access ;
    private Long points ;
    @OneToMany(mappedBy = "user_id")
    private Set<Found_Pieces> found_Pieces ;
    @OneToMany(mappedBy = "user_id")
    private Set<Completed_Riddles> completed_Riddles ;

    public Users(Long user_id, String login, String password, String email, String phone, String access, Long points) {
        this.user_id = user_id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.access = access;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(user_id, users.user_id) && Objects.equals(login, users.login) && Objects.equals(password, users.password) && Objects.equals(email, users.email) && Objects.equals(phone, users.phone) && Objects.equals(access, users.access) && Objects.equals(points, users.points) && Objects.equals(found_Pieces, users.found_Pieces) && Objects.equals(completed_Riddles, users.completed_Riddles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, login, password, email, phone, access, points, found_Pieces, completed_Riddles);
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Users() {
    }
}
