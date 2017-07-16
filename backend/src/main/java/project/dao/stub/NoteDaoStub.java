package project.dao.stub;


import org.springframework.stereotype.Service;
import project.dao.stub.util.IdGenerator;
import project.entity.Note;

import java.math.BigInteger;

@Service
public class NoteDaoStub extends BaseDaoStub<Note, BigInteger> implements NoteDao {

    @Override
    public <S extends Note> BigInteger getId(S entity) {
        if (entity.getId() != null) {
            return entity.getId();
        }
        BigInteger id = IdGenerator.BIG_INTEGER.generateId();
        entity.setId(id);
        return id;
    }

}
