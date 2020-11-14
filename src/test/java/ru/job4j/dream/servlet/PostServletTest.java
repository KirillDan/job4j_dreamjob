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

import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StoreStub;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class PostServletTest {
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
        Mockito.when(req.getParameter("name")).thenReturn("test1");
        Mockito.when(req.getParameter("description")).thenReturn("test1");

        HttpServletRequest req2 = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp2 = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req2.getParameter("id")).thenReturn("0");
        Mockito.when(req2.getParameter("name")).thenReturn("test2");
        Mockito.when(req2.getParameter("description")).thenReturn("test2");

        new PostServlet().doPost(req, resp);
        new PostServlet().doPost(req2, resp2);
        Iterator<Post> itr = store.findAllPosts().iterator();
        Post firstPost = itr.next();
        Post secondPost = itr.next();
        assertThat(firstPost.getName(), is("test1"));
        assertThat(secondPost.getName(), is("test2"));
        assertThat(firstPost.getId(), is(1));
        assertThat(secondPost.getId(), is(2));
    }
}
