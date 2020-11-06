package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.fileupload.FileItem;

public interface Store {
    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);
    
    void save(Candidate candidate);
    
    void save(User user);
    
    void deleteCandidate(Integer candidateId);
    
    void deleteUser(Integer userId);
    
    void save(int candidateId, List<FileItem> items);

    Post findById(int id);
    
    Candidate findByIdCandidate(int id);
    
    File findByIdPhoto(int id);
    
    User findByIdUser(int id);
}