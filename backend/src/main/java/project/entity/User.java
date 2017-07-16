package project.entity;


import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;
    private String name;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Note> createdNotes = new ArrayList<>();
    @ManyToMany(mappedBy = "editors", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Note> editableNotes = new ArrayList<>();

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getCreatedNotes() {
        return createdNotes;
    }

    public void setCreatedNotes(List<Note> createdNotes) {
        this.createdNotes = createdNotes;
    }

    public List<Note> getEditableNotes() {
        return editableNotes;
    }

    public void setEditableNotes(List<Note> editableNotes) {
        this.editableNotes = editableNotes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (createdNotes != null ? !createdNotes.equals(user.createdNotes) : user.createdNotes != null) return false;
        return editableNotes != null ? editableNotes.equals(user.editableNotes) : user.editableNotes == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createdNotes != null ? createdNotes.hashCode() : 0);
        result = 31 * result + (editableNotes != null ? editableNotes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
