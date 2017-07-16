package project.dao.impl;

import org.springframework.stereotype.Repository;
import project.entity.Note;

import java.math.BigInteger;

@Repository
public interface NoteRepository extends CustomRepository<Note, BigInteger>, NoteDao {
}
