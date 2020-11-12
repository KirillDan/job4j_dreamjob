package ru.job4j.dream.store;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

public class StoreStub implements Store {
	private final Map<Integer, Post> posts = new HashMap<>();
	private final Map<Integer, Candidate> candidates = new HashMap<>();
	private final Map<Integer, User> users = new HashMap<>();
    private int postIds = 0;
    private int candidateIds = 0;
    private int userIds = 0;
    private int photoIds = 0;
    
	private static final class Lazy {
		private static final Store INST = new StoreStub();
	}

	public static Store instOf() {
		return Lazy.INST;
	}
	
	@Override
	public Collection<Post> findAllPosts() {
		return this.posts.values();
	}

	@Override
	public Collection<Candidate> findAllCandidates() {
		return this.candidates.values();
	}

	@Override
	public void save(Post post) {
		post.setId(++this.postIds);
		this.posts.put(post.getId(), post);
	}

	@Override
	public void save(Candidate candidate) {
		candidate.setId(++this.candidateIds);
		this.candidates.put(candidate.getId(), candidate);
	}

	@Override
	public void save(User user) {
		user.setId(++this.userIds);
        this.users.put(user.getId(), user);
	}

	@Override
	public void save(int candidateId, List<FileItem> items) {
		Candidate candidate = this.candidates.get(candidateId);
		candidate.setPhotoId(++this.photoIds);
	}
	
	@Override
	public void deleteCandidate(Integer candidateId) {
		this.candidates.remove(candidateId);
	}

	@Override
	public void deleteUser(Integer userId) {
		this.users.remove(userId);
		
	}

	@Override
	public Post findById(int id) {
		return this.posts.get(id);
	}

	@Override
	public Candidate findByIdCandidate(int id) {
		return this.candidates.get(id);
	}

	@Override
	public User findByIdUser(int id) {
		return this.users.get(id);
	}

	@Override
	public User findByEmail(String userEmail) {
		User resUser = null;
		for (User user : this.users.values()) {
			if (user.getEmail().equals(userEmail)) {
				resUser = user;
			}
		}
		return resUser;
	}
	
	@Override
	public File findByIdPhoto(int candidateId) {
		return null;
	}
}