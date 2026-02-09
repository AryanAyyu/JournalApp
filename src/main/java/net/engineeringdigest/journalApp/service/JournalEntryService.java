package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){
        User user= userService.findByUsername(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);
    }
    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId myId){
        return journalEntryRepo.findById(myId);
    }

    @Transactional
    public boolean deleteById(ObjectId myId, String username){
        boolean removed = false;
        try {
            User user= userService.findByUsername(username);
            removed = user.getJournalEntries().removeIf(x->x.getId().equals(myId));
            if(removed){
                userService.saveUser(user);
                journalEntryRepo.deleteById(myId);
            }
        }catch(Exception e){
            throw new RuntimeException("An error occured while deleting the entry",e);
        }
        return removed;


    }

//    public List<JournalEntry> findByUsername(String username){
//
//    }


}
