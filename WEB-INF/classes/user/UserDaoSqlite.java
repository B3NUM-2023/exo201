package user;

import java.sql.*;


public class UserDaoSqlite implements UserDao {

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			throw new Error(e);
		}
	}

	protected Connection conn;
	public UserDaoSqlite( String userFilePath ) throws SQLException {

		//String jdbcUrl = "jdbc:sqlite:" + userFilePath;
		String jdbcUrl = "jdbc:sqlite:c:\\\\xampp\\\\tomcat\\\\webapps\\\\exo201\\\\WEB-INF\\\\users.db";
		this.conn = DriverManager.getConnection(jdbcUrl); //initialise la connexion
	}

	/**
	 * insére un nouvel utilisateur dans la base de données
	 * @param user l'utilisateur à ajouter/modifier
	 * @param password le mot de passe encodé
	 */
	@Override
	public void add(User user, String password) {
		try {
			String query = "INSERT INTO users (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setString(1, user.getFirstname());
			statement.setString(2, user.getLastname());
			statement.setString(3, user.getEmail());
			statement.setString(4, password);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * met à jour les informations d'un utilisateur existant
	 * @param user l'utilisateur à ajouter/modifier
	 * @param password nouveau mot de passe si il est fourni.
	 */
	@Override
	public void update(User user, String password) {
		try {
			String updateQuery = "UPDATE users SET firstname = ?, lastname = ?, email = ?, password = ? WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(updateQuery);
			statement.setString(1, user.getFirstname());
			statement.setString(2, user.getLastname());
			statement.setString(3, user.getEmail());
			statement.setString(4, password);
			statement.setLong(5, user.getId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * recherche un utilisateur par son ID
	 * @param id
	 * @return
	 */
	@Override
	public User find(long id) {
		try {
			String selectQuery = "SELECT firstname, lastname, email, password FROM users WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(selectQuery);
			statement.setLong(1, id);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");

				return new User(id, firstname, lastname, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * recherche un utilisateur par son email
	 * @param email
	 * @return
	 */
	@Override
	public User findByEmail(String email) {
		try {
			String selectQuery = "SELECT id, firstname, lastname, password FROM users WHERE email = ?";
			PreparedStatement statement = conn.prepareStatement(selectQuery);
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				long id = resultSet.getLong("id");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				String password = resultSet.getString("password");

				return new User(id, firstname, lastname, email, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * vérifie si le mot de passe correspond à l'email donné
	 * @param email adresse mail de l'uditisateur (login)
	 * @param password mot de passe de l'utilisateur.
	 * @return
	 */
	@Override
	public long checkPassword(String email, String password) {
		try {
			String selectQuery = "SELECT id FROM users WHERE email = ? AND password = ?";
			PreparedStatement statement = conn.prepareStatement(selectQuery);
			statement.setString(1, email);
			statement.setString(2, password);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getLong("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1; //aucune correspondance trouvée
	}

	/**
	 * supprime un utilisateur par son ID
	 * @param id identifiant de l'utilisateur
	 */
	@Override
	public void delete(long id) {
		try {
			String deleteQuery = "DELETE FROM users WHERE id = ?";
			PreparedStatement statement = conn.prepareStatement(deleteQuery);
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * vérifie si un utilisateur avec l'email donné existe
	 * @param email
	 * @return
	 */
	@Override
	public long exists(String email) {
		try {
			String selectQuery = "SELECT id FROM users WHERE email = ?";
			PreparedStatement statement = conn.prepareStatement(selectQuery);
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getLong("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return -1; //aucun utilisateur avec cet email
	}

}
