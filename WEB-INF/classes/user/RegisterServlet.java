package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/register")
public class RegisterServlet extends HttpServlet {
	
	private UserDao userDao;
	
	@Override
	public void init() throws ServletException {
		try {
			// Initialisation du DAO : UserDAOSqlite
			String userFilePath = "../WEB-INF/users.db";
			this.userDao = new UserDaoSqlite(userFilePath);
		} catch (Exception e) {
			throw new ServletException("Erreur lors de l'initialisation du DAO", e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//récupération des paramètres du formulaire d'inscription
		String firstname = req.getParameter("firstname");
		String lastname = req.getParameter("lastname");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirmPassword");

		//vérification des mots de passe
		if (!password.equals(confirmPassword)) {
			resp.sendRedirect("register.jsp?error=Les+mots+de+passe+ne+correspondent+pas");
			return;
		}

		//création d'un nouvel utilisateur et ajout en utilisant le DAO
		User user = new User(0, firstname, lastname, email, password); //l'ID sera généré par la base de données
		userDao.add(user, password);

		//redirection vers register.jsp
		resp.sendRedirect(req.getContextPath() + "/register.jsp");
	}

}
