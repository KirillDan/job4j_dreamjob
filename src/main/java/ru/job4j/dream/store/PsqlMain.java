package ru.job4j.dream.store;

import java.time.LocalDate;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

public class PsqlMain {
	public static void main(String[] args) {
		Store store = PsqlStore.instOf();
		Post post = new Post(0, "Java Job", "Working with Servlets", LocalDate.now().toString());
		store.save(post);
		for (Post postI : store.findAllPosts()) {
			System.out.println(postI.getId() + "   " + postI.getName() + "   " + postI.getDescription());
			postI.setName("new Name");
			store.save(postI);
		}
		System.out.println();
		Post postId = store.findById(1);
		System.out.println(postId.getId() + "   " + postId.getName() + "   " + postId.getDescription());
		System.out.println();
		Candidate candidate = new Candidate(0, "Pupkin", "Vasua", "Midlle");
		store.save(candidate);
		for (Candidate candidateI : store.findAllCandidates()) {
			System.out
					.println(candidateI.getId() + "   " + candidateI.getFirstname() + "   " + candidateI.getLastname());
			candidateI.setFirstname("new Name");
			store.save(candidateI);
		}
		System.out.println();
		Candidate candidateId = store.findByIdCandidate(1);
		System.out
				.println(candidateId.getId() + "   " + candidateId.getFirstname() + "   " + candidateId.getLastname());
	}
}
