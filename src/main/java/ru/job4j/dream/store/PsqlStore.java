package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.fileupload.FileItem;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
	private static final String SQL_DELETE_BY_ID_CANDIDATE = "DELETE FROM candidate WHERE id = ?";
	
	private static final String SQL_DROP_PHOTO = "DROP TABLE IF EXISTS phote";
	private static final String SQL_CREATE_PHOTO = "CREATE TABLE phote (id SERIAL PRIMARY KEY, candidate_id SERIAL, FOREIGN KEY (candidate_id) REFERENCES candidate(id))";
	private static final String SQL_INSERT_PHOTO = "INSERT INTO phote(candidate_id) VALUES (?)";
	private static final String SQL_FIND_BY_ID_PHOTO = "SELECT * FROM phote WHERE candidate_id = ?";
	private static final String SQL_DELETE_BY_CANDIDATE_ID_PHOTO = "DELETE FROM phote WHERE candidate_id = ?";
	
	private static final String SQL_DROP_USER = "DROP TABLE IF EXISTS users";
	private static final String SQL_CREATE_USER = "CREATE TABLE users(id SERIAL PRIMARY KEY, name TEXT, email TEXT, password TEXT)";
	private static final String SQL_INSERT_USER = "INSERT INTO users(name, email, password) VALUES (?, ?, ?)";
	private static final String SQL_FIND_BY_ID_USER = "SELECT * FROM users WHERE id = ?";
	private static final String SQL_FIND_BY_EMAIL_USER = "SELECT * FROM users WHERE email = ?";
	private static final String SQL_UPDATE_BY_ID_USER = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
	private static final String SQL_DELETE_BY_ID_USER = "DELETE FROM users WHERE id = ?";
	
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
//		this.createTablePost();
//		this.createTableCandidates();
//		this.createTablePhoto();
//		this.createTableUser();
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
			logger.error("Не удалось пересоздать таблицу post", e);
		}
	}

	private void createTableCandidates() {
		try (Connection connection = this.pool.getConnection()) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(SQL_DROP_CANDIDATE);
			statement.executeUpdate(SQL_CREATE_CANDIDATE);
		} catch (SQLException e) {
			logger.error("Не удалось пересоздать таблицу candidates", e);
		}
	}
	
	private void createTablePhoto() {
		try (Connection connection = this.pool.getConnection()) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(SQL_DROP_PHOTO);
			statement.executeUpdate(SQL_CREATE_PHOTO);
		} catch (SQLException e) {
			logger.error("Не удалось пересоздать таблицу photo", e);
		}
	}

	private void createTableUser() {
		try (Connection connection = this.pool.getConnection()) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(SQL_DROP_USER);
			statement.executeUpdate(SQL_CREATE_USER);
		} catch (SQLException e) {
			logger.error("Не удалось пересоздать таблицу user", e);
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
		} catch (SQLException e) {
			logger.error("Не удалось найти все строки таблицы post", e);
		}
		return posts;
	}

	@Override
	public Collection<Candidate> findAllCandidates() {
		List<Candidate> candidates = new ArrayList<>();
		try (Connection cn = pool.getConnection(); Statement st = cn.createStatement()) {
			try (ResultSet it = st.executeQuery(SQL_SELECT_ALL_CANDIDATE)) {
				while (it.next()) {
					Candidate candidate = new Candidate(it.getInt("id"), it.getString("firstname"), it.getString("lastname"),
							it.getString("position"));
					try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_ID_PHOTO);){
						ps.setInt(1, it.getInt("id"));
						ps.execute();
						try(ResultSet itp = ps.getResultSet()) {
							if(itp.next()) {
								candidate.setPhotoId(itp.getInt("id"));
							}
						}
					}
					candidates.add(candidate);
				}
			}
		} catch (Exception e) {
			logger.error("Не удалось найти все строки таблицы candidate", e);
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
	
	@Override
	public void save(int candidateId, List<FileItem> items) {
		if (candidateId != 0) {
			create(candidateId, items);
		}
	}

	@Override
	public void save(User user) {
		if (user.getId() == 0) {
			create(user);
		} else {
			update(user);
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
			logger.error("Не удалось добавить строку в таблицу post", e);
		}
		return post;
	}
	
	private User create(User user) {
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.execute();
			try (ResultSet id = ps.getGeneratedKeys()) {
				if (id.next()) {
					user.setId(id.getInt("id"));
				}
			}
		} catch (Exception e) {
			logger.error("Не удалось добавить строку в таблицу users", e);
		}
		return user;
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
			logger.error("Не удалось добавить строку в таблицу candidate", e);
		}
		return candidate;
	}
	
	private Integer create(int candidateId, List<FileItem> items) {
		Integer dbId = null;
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_INSERT_PHOTO,
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, candidateId);
			ps.execute();
			try (ResultSet id = ps.getGeneratedKeys()) {
				if (id.next()) {
					dbId = id.getInt("id");
					savePhoto(items, dbId);
				}
			} catch (IOException e) {
				logger.error("Не удалось сохранить фото в файловой системе", e);
			}
		} catch (SQLException e) {
			logger.error("Не удалось добавить строку в таблицу photo", e);
		}
		return dbId;
	}
	
	private void savePhoto(List<FileItem> items, int photoId) throws IOException {
		File folder = new File("bin" + File.separator + "images" + File.separator + "phote_id");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		for (FileItem item : items) {
			if (!item.isFormField()) {
				File file = new File(folder + File.separator + photoId);
				try (FileOutputStream out = new FileOutputStream(file)) {
					out.write(item.getInputStream().readAllBytes());
				}
				break;
			}
		}
	}

	private void update(Post post) {
		try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_BY_ID_POST);) {
			ps.setString(1, post.getName());
			ps.setString(2, post.getDescription());
			ps.setInt(3, post.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Не удалось обновить строку в таблице post", e);
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
			logger.error("Не удалось обновить строку в таблице candidate", e);
		}
	}
	
	private void update(User user) {
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_BY_ID_USER);) {
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setInt(4, user.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Не удалось обновить строку в таблице users", e);
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
			logger.error("Не удалось найти строку в таблице post", e);
		}
		return post;
	}

	@Override
	public Candidate findByIdCandidate(int id) {
		Candidate candidate = null;
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_ID_CANDIDATE)) {
			ps.setInt(1, id);
			ps.execute();
			ResultSet resultSet = ps.getResultSet();
			if (resultSet.next()) {
				candidate = new Candidate(resultSet.getInt("id"), resultSet.getString("firstname"),
						resultSet.getString("lastname"), resultSet.getString("position"));
			}
		} catch (SQLException e) {
			logger.error("Не удалось найти строку в таблице candidate", e);
		}
		return candidate;
	}
	
	@Override
	public User findByIdUser(int id) {
		User user = null;
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_ID_USER)) {
			ps.setInt(1, id);
			ps.execute();
			ResultSet resultSet = ps.getResultSet();
			if (resultSet.next()) {
				user = new User(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getString("email"), resultSet.getString("password"));
			}
		} catch (SQLException e) {
			logger.error("Не удалось найти строку в таблице users", e);
		}
		return user;
	}

	@Override
	public User findByEmail(String userEmail) {
		User user = null;
		try (Connection cn = pool.getConnection();
				PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_EMAIL_USER);) {
			ps.setString(1, userEmail);
			ps.execute();
			ResultSet resultSet = ps.getResultSet();
			if (resultSet.next()) {
				user = new User(resultSet.getInt("id"), resultSet.getString("name"),
						resultSet.getString("email"), resultSet.getString("password"));
			}
		} catch (SQLException e) {
			logger.error("Не удалось найти строку в таблице users по email", e);
		}
		return user;
	}

	@Override
	public File findByIdPhoto(int candidateId) {
		File file = null;
		try (Connection cn = pool.getConnection(); PreparedStatement ps = cn.prepareStatement(SQL_FIND_BY_ID_CANDIDATE);) {
			ps.setInt(1, candidateId);
			ps.execute();
			try(ResultSet resultSet = ps.getResultSet()) {
				if (resultSet.next()) {
					int photoId = resultSet.getInt("id");
					file = new File("bin" + File.separator + "images" + File.separator + "phote_id" + File.separator + photoId);
				}
			}
		} catch (SQLException e) {
			logger.error("Не удалось найти строку в таблице photo", e);
		}
		return file;
	}

	@Override
	public void deleteCandidate(Integer candidateId) {
		try (Connection cn = pool.getConnection(); 
				PreparedStatement pssp = cn.prepareStatement(SQL_FIND_BY_ID_PHOTO);
				PreparedStatement psdc = cn.prepareStatement(SQL_DELETE_BY_ID_CANDIDATE);
				PreparedStatement psdp = cn.prepareStatement(SQL_DELETE_BY_CANDIDATE_ID_PHOTO);) {
			pssp.setInt(1, candidateId);
			pssp.execute();
			try (ResultSet resultSet = pssp.getResultSet()) {
				while (resultSet.next()) {
					int photoId = resultSet.getInt("id");
					File file = new File("bin" + File.separator + "images" + File.separator + "phote_id" + File.separator + photoId);
					file.delete();
				}
			}
			psdp.setInt(1,candidateId);
			psdp.executeUpdate();
			psdc.setInt(1,candidateId);
			psdc.executeUpdate();
		} catch (SQLException e) {
			logger.error("Не удалось удалить строку в таблице candidate", e);
		}
	}

	@Override
	public void deleteUser(Integer userId) {
		try (Connection cn = pool.getConnection(); 
				PreparedStatement ps = cn.prepareStatement(SQL_DELETE_BY_ID_USER);) {
			ps.setInt(1, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("Не удалось удалить строку в таблице users", e);
		}
	}
}
