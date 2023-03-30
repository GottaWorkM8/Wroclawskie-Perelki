package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Completed_Riddles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long completed_riddle_id ;
    @ManyToOne
    private Riddles riddle_id ;
    @ManyToOne
    private Users user_id  ;
    private LocalDate date   ;

    public void setCompleted_riddle_id(Long completed_riddle_id) {
        this.completed_riddle_id = completed_riddle_id;
    }

    public void setRiddle_id(Riddles riddle_id) {
        this.riddle_id = riddle_id;
    }

    public void setUser_id(Users user_id) {
        this.user_id = user_id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getCompleted_riddle_id() {
        return completed_riddle_id;
    }

    public Riddles getRiddle_id() {
        return riddle_id;
    }

    public Users getUser_id() {
        return user_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Completed_Riddles(Long completed_riddle_id, Riddles riddle_id, Users user_id, LocalDate date) {
        this.completed_riddle_id = completed_riddle_id;
        this.riddle_id = riddle_id;
        this.user_id = user_id;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Completed_Riddles that = (Completed_Riddles) o;
        return Objects.equals(completed_riddle_id, that.completed_riddle_id) && Objects.equals(riddle_id, that.riddle_id) && Objects.equals(user_id, that.user_id) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completed_riddle_id, riddle_id, user_id, date);
    }

    public Completed_Riddles() {
    }
}
