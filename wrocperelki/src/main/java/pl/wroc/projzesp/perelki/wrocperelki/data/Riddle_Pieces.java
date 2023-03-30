package pl.wroc.projzesp.perelki.wrocperelki.data;

import jakarta.persistence.*;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Objects;
import java.util.Set;

@Entity
public class Riddle_Pieces {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riddle_piece_id;
    @ManyToOne
    private Riddles riddle_id;
    @ManyToOne
    private Pieces piece_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Riddle_Pieces that = (Riddle_Pieces) o;
        return Objects.equals(riddle_piece_id, that.riddle_piece_id) && Objects.equals(riddle_id, that.riddle_id) && Objects.equals(piece_id, that.piece_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riddle_piece_id, riddle_id, piece_id);
    }

    public Riddle_Pieces() {
    }

    public Riddle_Pieces(Long riddle_piece_id, Riddles riddle_id, Pieces piece_id) {
        this.riddle_piece_id = riddle_piece_id;
        this.riddle_id = riddle_id;
        this.piece_id = piece_id;
    }

    public Long getRiddle_piece_id() {
        return riddle_piece_id;
    }

    public void setRiddle_piece_id(Long riddle_piece_id) {
        this.riddle_piece_id = riddle_piece_id;
    }

    public Riddles getRiddle_id() {
        return riddle_id;
    }

    public void setRiddle_id(Riddles riddle_id) {
        this.riddle_id = riddle_id;
    }

    public Pieces getPiece_id() {
        return piece_id;
    }

    public void setPiece_id(Pieces piece_id) {
        this.piece_id = piece_id;
    }
}
