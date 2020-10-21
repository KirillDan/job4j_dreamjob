package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.sql.Date;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    
    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Development of algorithms for processing entities", new Date(120, 7, 5).toString()));
        posts.put(2, new Post(2, "Middle Java Job", "Web development", new Date(120, 6, 27).toString()));
        posts.put(3, new Post(3, "Senior Java Job", "Development of a distributed server part of the program", new Date(120, 7, 15).toString()));
        posts.put(4, new Post(4, "Senior/Tech Lead Java Job", "Development team management", new Date(120, 5, 15).toString()));
        posts.put(5, new Post(5, "Tech Lead/Architect Java Job", "Application architecture creation", new Date(120, 8, 7).toString()));
        candidates.put(1, new Candidate(1, "Vasya", "Pupkin", "Junior Java"));
        candidates.put(2, new Candidate(2, "Victor", "Pupkin", "Middle Java"));
        candidates.put(3, new Candidate(3, "Andy", "Pupkin", "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }
}