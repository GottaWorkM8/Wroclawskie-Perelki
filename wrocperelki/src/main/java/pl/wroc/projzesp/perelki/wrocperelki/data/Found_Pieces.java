package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Found_Pieces {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long completed_riddle_id ;
    @ManyToOne
    private Pieces piece_id ;
    @ManyToOne
    private Users user_id  ;
    private LocalDate date   ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Found_Pieces that = (Found_Pieces) o;
        return Objects.equals(completed_riddle_id, that.completed_riddle_id) && Objects.equals(piece_id, that.piece_id) && Objects.equals(user_id, that.user_id) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completed_riddle_id, piece_id, user_id, date);
    }

    public Found_Pieces() {
    }

    public Found_Pieces(Long completed_riddle_id, Pieces piece_id, Users user_id, LocalDate date) {
        this.completed_riddle_id = completed_riddle_id;
        this.piece_id = piece_id;
        this.user_id = user_id;
        this.date = date;
    }

    public Long getCompleted_riddle_id() {
        return completed_riddle_id;
    }

    public void setCompleted_riddle_id(Long completed_riddle_id) {
        this.completed_riddle_id = completed_riddle_id;
    }

    public Pieces getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(Pieces piece_id) {
        this.piece_id = piece_id;
    }

    public Users getUser_id() {
        return user_id;
    }

    public void setUser_id(Users user_id) {
        this.user_id = user_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
