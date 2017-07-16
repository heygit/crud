package project.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dao.impl.NoteDao;
import project.dao.impl.UserDao;
import project.entity.Note;
import project.entity.User;
import project.service.NoteService;

import java.math.BigInteger;
import java.util.Collections;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteDao noteDao;
    private final UserDao userDao;

    @Autowired
    public NoteServiceImpl(NoteDao noteDao, UserDao userDao) {
        this.noteDao = noteDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void updateNote() {
        BigInteger id = new BigInteger("2");
        Note note = noteDao.findOneForUpdate(id);
        long created = note.getCreated();
        note.setCreated(created*3);
        noteDao.save(note);
    }

    @Override
    @Transactional
    public void createNotes(boolean ex) {
        makePair();

        if (ex) {
            Object ob = null;
            ob.toString();
        }

    }

    private void makePair() {
        User user = getUser();

        Note createdNote = getNote();
        user.getCreatedNotes().add(createdNote);
        createdNote.setAuthor(user);

        Note editableNote = getNote();
        user.getEditableNotes().add(editableNote);
        editableNote.setEditors(Collections.singletonList(user));

        userDao.save(user);
    }

    private User getUser() {
        String time = System.currentTimeMillis()+"";
        User user = new User();
        user.setName(time);
        return user;
    }

    private Note getNote() {
        String time = System.currentTimeMillis()+"";
        Note note = new Note();
        note.setName(time);
        note.setDetails(time);
        note.setCreated(3);
        return note;
    }


}
