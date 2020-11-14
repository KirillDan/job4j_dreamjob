package ru.job4j.dream.servlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;
import ru.job4j.dream.store.StoreStub;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlStore.class)
public class RegServletTest {
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
        HttpSession session = Mockito.mock(HttpSession.class);
        Mockito.when(req.getSession()).thenReturn(session);
        
        Mockito.when(req.getParameter("name")).thenReturn("test1");
        Mockito.when(req.getParameter("email")).thenReturn("test1");
        Mockito.when(req.getParameter("password")).thenReturn("test1");
        HttpServletRequest req2 = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp2 = Mockito.mock(HttpServletResponse.class);
        Mockito.when(req2.getSession()).thenReturn(session);

        Mockito.when(req2.getParameter("name")).thenReturn("test2");
        Mockito.when(req2.getParameter("email")).thenReturn("test2");
        Mockito.when(req2.getParameter("password")).thenReturn("test2");
        new RegServlet().doPost(req, resp);
        new RegServlet().doPost(req2, resp2);
        
        User firstUser = store.findByIdUser(1);
        User secondUser = store.findByIdUser(2);
        
        assertThat(firstUser.getName(), is("test1"));
        assertThat(secondUser.getName(), is("test2"));
        assertThat(firstUser.getId(), is(1));
        assertThat(secondUser.getId(), is(2));
        store.deleteUser(1);
        assertEquals(store.findByIdUser(1), null);
        store.deleteUser(2);
        assertEquals(store.findByIdUser(2), null);
    }
}
