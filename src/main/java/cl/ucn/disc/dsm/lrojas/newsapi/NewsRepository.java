package cl.ucn.disc.dsm.lrojas.newsapi;

import cl.ucn.disc.dsm.lrojas.newsapi.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Repository of News
 * @author Lixo
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

}
