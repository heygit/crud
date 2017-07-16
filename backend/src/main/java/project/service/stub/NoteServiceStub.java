package project.service.stub;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dao.stub.NoteDao;
import project.dao.stub.UserDao;
import project.entity.Note;
import project.service.NoteService;

@Service
public class NoteServiceStub implements NoteService {

    private final NoteDao noteDao;
    private final UserDao userDao;

    @Autowired
    public NoteServiceStub(NoteDao noteDao, UserDao userDao) {
        this.noteDao = noteDao;
        this.userDao = userDao;
    }

    @Override
    public void updateNote() {

    }

    @Override
    public void createNotes(boolean ex) {
        noteDao.save(new Note());
    }
}
