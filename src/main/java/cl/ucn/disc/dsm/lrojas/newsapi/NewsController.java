package cl.ucn.disc.dsm.lrojas.newsapi;

import cl.ucn.disc.dsm.lrojas.newsapi.model.News;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The Controller of News
 * @author Lixxxo
 */
@RestController
public class NewsController {

    /**
     * The Repo of News
     */
    private final NewsRepository newsRepository;

    /**
     * The repo of News
     */
    public NewsController(NewsRepository newsRepository){
        this.newsRepository = newsRepository;
    }
    /**
     * Return all News in the Backend
     */
    @GetMapping("v1/news")
    public List<News> all(){

        // Equals to SELECT * FROM News;
        final List<News> theNews = this.newsRepository.findAll();
        return theNews;
    }

    /*
    Return one new given its id.
     */
    @GetMapping("v1/news/{id}")
    public News one(@PathVariable final Long id){
        // FIXME: Change the RuntimeException to 404
        return this.newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News Not Found :("));
    }
}
