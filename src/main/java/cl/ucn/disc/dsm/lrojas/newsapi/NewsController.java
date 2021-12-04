package cl.ucn.disc.dsm.lrojas.newsapi;

import cl.ucn.disc.dsm.lrojas.newsapi.model.News;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * The Controller of News
 * @author Lixxxo
 */
@RestController
public class NewsController {

    /*
    Return all News in the Backend
     */
    @GetMapping("v1/news")
    public List<News> all(){

        return new ArrayList<>();
    }

    /*
    Return one new given its id.
     */
    @GetMapping("v1/news/{id}")
    public News one(@PathVariable final Long id){

        return new News();
    }
}
