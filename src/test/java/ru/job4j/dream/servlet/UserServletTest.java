package ru.job4j.dream.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StoreStub;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class UserServletTest {
	private Store store;
	
	@Before
	public void setup() {
		this.store = new StoreStub();
		PowerMockito.mockStatic(PsqlStore.class);
	}
	
	@Test
    public void whenAddUserThenStoreItThenDelete() throws ServletException, IOException, InterruptedException {
        Mockito.when(PsqlStore.instOf()).thenReturn(store);
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req.getParameter("id")).thenReturn("0");
        Mockito.when(req.getParameter("firstname")).thenReturn("test1");
        Mockito.when(req.getParameter("lastname")).thenReturn("test1");
        Mockito.when(req.getParameter("position")).thenReturn("test1");
        HttpServletRequest req2 = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp2 = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req2.getParameter("id")).thenReturn("0");
        Mockito.when(req2.getParameter("firstname")).thenReturn("test2");
        Mockito.when(req2.getParameter("lastname")).thenReturn("test2");
        Mockito.when(req2.getParameter("position")).thenReturn("test2");
        new CandidateServlet().doPost(req, resp);
        new CandidateServlet().doPost(req2, resp2);
        Iterator<Candidate> itr = store.findAllCandidates().iterator();
        Candidate firstCandidate = itr.next();
        Candidate secondCandidate = itr.next();
        assertThat(firstCandidate.getFirstname(), is("test1"));
        assertThat(secondCandidate.getFirstname(), is("test2"));
        assertThat(firstCandidate.getId(), is(1));
        assertThat(secondCandidate.getId(), is(2));
        assertThat(store.findByIdCandidate(1).getFirstname(), is("test1"));
        assertThat(store.findByIdCandidate(2).getFirstname(), is("test2"));
        store.deleteCandidate(1);
        assertThat(store.findAllCandidates().size(), is(1));
        store.deleteCandidate(2);
        assertThat(store.findAllCandidates().size(), is(0));
    }
}
