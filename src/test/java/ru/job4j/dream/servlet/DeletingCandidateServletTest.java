package ru.job4j.dream.servlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StoreStub;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class DeletingCandidateServletTest {
private Store store;
	
	@Before
	public void setup() {
		this.store = new StoreStub();
		PowerMockito.mockStatic(PsqlStore.class);
	}
	
	@Test
    public void whenAddUserThenStoreItThenDelete() throws ServletException, IOException, InterruptedException {
        Mockito.when(PsqlStore.instOf()).thenReturn(store);
        store.save(new Candidate(0,"test1","test1","test1"));
        store.save(new Candidate(0,"test2","test2","test2"));
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("id")).thenReturn("1");
        HttpServletRequest req2 = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp2 = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req2.getParameter("id")).thenReturn("2");            
        Iterator<Candidate> itr = store.findAllCandidates().iterator();
        Candidate firstCandidate = itr.next();
        Candidate secondCandidate = itr.next();
        assertThat(firstCandidate.getFirstname(), is("test1"));
        assertThat(secondCandidate.getFirstname(), is("test2"));
        assertThat(firstCandidate.getId(), is(1));
        assertThat(secondCandidate.getId(), is(2));
        assertThat(store.findByIdCandidate(1).getFirstname(), is("test1"));
        assertThat(store.findByIdCandidate(2).getFirstname(), is("test2"));
        new DeletingCandidateServlet().doGet(req, resp);
        assertThat(store.findAllCandidates().size(), is(1));
        new DeletingCandidateServlet().doGet(req2, resp2); 
        assertThat(store.findAllCandidates().size(), is(0));
    }
}
