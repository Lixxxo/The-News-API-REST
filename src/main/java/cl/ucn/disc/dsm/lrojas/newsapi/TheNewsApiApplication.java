package cl.ucn.disc.dsm.lrojas.newsapi;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TheNewsApiApplication {

	/*** The {@link NewsRepository} used to initialize the database
	 *
	 */
	@Autowired
	private NewsRepository newsRepository;

	/**
	 * The Main starting point.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TheNewsApiApplication.class, args);
	}

	/**
	 * Initialize the data inside the Database
	 * @return the data to use
	 */
	@Bean
	protected InitializingBean initializingDatabase(){
		return () -> {

		};
	}
}
