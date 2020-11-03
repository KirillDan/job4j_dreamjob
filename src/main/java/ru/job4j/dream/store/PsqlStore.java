package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PsqlStore implements Store {
	private static Logger logger = LogManager.getLogger(PsqlStore.class);

	private static final String SQL_DROP_POST = "DROP TABLE IF EXISTS post";
	private static final String SQL_CREATE_POST = "CREATE TABLE post (id SERIAL PRIMARY KEY, post_name TEXT, description TEXT, created TEXT)";
	private static final String SQL_INSERT_POST = "INSERT INTO post(post_name, description, created) VALUES (?, ?, ?)";
	private static final String SQL_FIND_BY_ID_POST = "SELECT * FROM post WHERE id = ?";
	private static final String SQL_UPDATE_BY_ID_POST = "UPDATE post SET post_name = ?, description = ? WHERE id = ?";
	private static final String SQL_SELECT_ALL_POST = "SELECT * FROM post";

	private static final String SQL_DROP_CANDIDATE = "DROP TABLE IF EXISTS candidate";
	private static final String SQL_CREATE_CANDIDATE = "CREATE TABLE candidate (id SERIAL PRIMARY KEY, firstname TEXT, lastname TEXT, position TEXT)";
	private static final String SQL_INSERT_CANDIDATE = "INSERT INTO candidate(firstname, lastname, position) VALUES (?, ?, ?)";
	private static final String SQL_FIND_BY_ID_CANDIDATE = "SELECT * FROM candidate WHERE id = ?";
	private static final String SQL_UPDATE_BY_ID_CANDIDATE = "UPDATE candidate SET firstname = ?, lastname = ?, position = ? WHERE id = ?";
	private static final String SQL_SELECT_ALL_CANDIDATE = "SELECT * FROM candidate";

	private final BasicDataSource pool = new BasicDataSource();

	private PsqlStore() {
		Properties cfg = new Properties();
		try (BufferedReader io = new BufferedReader(new FileReader(
				"/home/kirill/Documents/Java/workspaces/eclipse-workspace-examples2/job4j_dreamjob/db.properties"))) {
			cfg.load(io);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		try {
			Class.forName(cfg.getProperty("jdbc.driver"));
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
		pool.setUrl(cfg.getProperty("jdbc.url"));
		pool.setUsername(cfg.getProperty("jdbc.username"));
		pool.setPassword(cfg.getProperty("jdbc.password"));
		pool.setMinIdle(5);
		pool.setMaxIdle(10);
		pool.setMaxOpenPreparedStatements(100);
		this.createTablePost();
		this.createTableCandidates();
	}

	private static final class Lazy {
		private static final Store INST = new PsqlStore();
	}

	public static Store instOf() {
		return Lazy.INST;
	}

	private void createTablePost() {
		try (Connection connection = this.pool.getConnection()) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(SQL_DROP_POST);
			statement.executeUpdate(SQL_CREATE_POST);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	private void createTableCandidates() {
		try (Connection connection = this.pool.getConnection()) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(SQL_DROP_CANDIDATE);
			statement.executeUpdate(SQL_CREATE_CANDIDATE);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Collection<Post> findAllPosts() {
		List<Post> posts = new ArrayList<>();
		try (Connection cn = pool.getConnection(); Statement ps = cn.createStatement();) {
			try (ResultSet it = ps.executeQuery(SQL_SELECT_ALL_POST)) {
				while (it.next()) {
					posts.add(new Post(it.getInt("id"), it.getString("post_name"), it.getString("description"),
							it.getString("created")));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return posts;
	}

	@Override
	public Collection<Candidate> findAllCandidates() {
		List<Candidate> candidates = new ArrayList<>();
		try (Connection cn = pool.getConnection(); Statement ps = cn.createStatement();) {
			try (ResultSet it = ps.executeQuery(SQL_SELECT_ALL_CANDIDATE)) {
				while (it.next()) {
					candidates.add(new Candidate(it.getInt("id"), it.getString("firstname"), it.getString("lastname"),
							it.getString("position")));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return candidates;
	}

	@Override
	public void save(Post post) {
		if (post.getId() == 0) {
			create(post);
		} else {
			update(post);
		}
	}

	@Override
	public void save(Candidate candidate) {
		if (candidate.getId() == 0) {
			create(candidate);
		} else {
			update(candidate);
		}
	}

	private Post create(Post post) {
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_INSERT_POST, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, post.getName());
			ps.setString(2, post.getDescription());
			ps.setString(3, post.getCreated());
			ps.execute();
			try (ResultSet id = ps.getGeneratedKeys()) {
				if (id.next()) {
					post.setId(id.getInt("id"));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return post;
	}

	private Candidate create(Candidate candidate) {
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_INSERT_CANDIDATE,
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, candidate.getFirstname());
			ps.setString(2, candidate.getLastname());
			ps.setString(3, candidate.getPosition());
			ps.execute();
			try (ResultSet id = ps.getGeneratedKeys()) {
				if (id.next()) {
					candidate.setId(id.getInt("id"));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return candidate;
	}

	private void update(Post post) {
		try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_BY_ID_POST);) {
			ps.setString(1, post.getName());
			ps.setString(2, post.getDescription());
			ps.setInt(3, post.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	private void update(Candidate candidate) {
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_BY_ID_CANDIDATE);) {
			ps.setString(1, candidate.getFirstname());
			ps.setString(2, candidate.getLastname());
			ps.setString(3, candidate.getPosition());
			ps.setInt(4, candidate.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@Override
	public Post findById(int id) {
		Post post = null;
		try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_ID_POST);) {
			ps.setInt(1, id);
			ps.execute();
			ResultSet resultSet = ps.getResultSet();
			if (resultSet.next()) {
				post = new Post(resultSet.getInt("id"), resultSet.getString("post_name"),
						resultSet.getString("description"), resultSet.getString("created"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return post;
	}

	@Override
	public Candidate findByIdCandidate(int id) {
		Candidate candidate = null;
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_ID_CANDIDATE);) {
			ps.setInt(1, id);
			ps.execute();
			ResultSet resultSet = ps.getResultSet();
			if (resultSet.next()) {
				candidate = new Candidate(resultSet.getInt("id"), resultSet.getString("firstname"),
						resultSet.getString("lastname"), resultSet.getString("position"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return candidate;
	}
}
