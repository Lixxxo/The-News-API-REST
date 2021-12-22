package cl.ucn.disc.dsm.lrojas.newsapi;

import cl.ucn.disc.dsm.lrojas.newsapi.model.News;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.network.APIClient;
import com.kwabenaberko.newsapilib.network.APIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Response;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Controller of News
 * @author Lixxxo
 */
@Slf4j
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
    public List<News> all(@RequestParam(required = false, defaultValue = "false") Boolean reload){

        // Is reload -> get news from NewsAPI.org
        if(reload){
            this.reloadNewsFromNewsApi();
        }
        // http://host:port/v1/news?reload=true
        log.info("Request all with reload: {}.", reload);
        // Equals to SELECT * FROM News;
        final List<News> theNews = this.newsRepository.findAll();
        return theNews;
    }

    /**
     * Get the News from the NewsAPI and save the records in the database.
     */
    private void reloadNewsFromNewsApi() {

        // WARNING: JUST for testing
        final String API_KEY = "85b341510d8741c6b100b03f18ac5c9b";
        final String category = "health";
        final int pageSize = 50;
        final String country = "ru";
        // 1. Create the APIService from APIClient
        final APIService apiService = APIClient.getAPIService();

        // 2. Build http request params
        final Map<String, String> reqParams = new HashMap<>();
        // The API key
        reqParams.put("apiKey", API_KEY);
        // Filter by category
        reqParams.put("category", category);
        // Filter by country
        reqParams.put("country", country);
        // The number of results per page (20 = min, 100 = max)
        reqParams.put("pageSize", String.valueOf(pageSize));

        // 3. Call the Request
        try {
            Response<ArticleResponse> articlesResponse =
                    apiService.getTopHeadlines(reqParams).execute();

            // Success !
            if(articlesResponse.isSuccessful()){

                List<Article> articles = articlesResponse.body().getArticles();

                List<News> news = new ArrayList<>();
                // Conversion for List<Article> -> List<News>
                for (Article article: articles) {
                    // FIXME: method always returns false
                    // validate if News exist in NewsRepository
                    News n = toNews(article);
                    if(!this.newsRepository.existsById(n.getId())){
                        news.add(n);
                    }

                }

                // 4. Save into the local database
                this.newsRepository.saveAll(news);

            }
        } catch (IOException e) {
            log.error("Getting the Articles from NewsAPI", e);
        }

    }

    /**
     * Convert Article to News
     *
     * @param article to convert
     * @return the News converted
     */
    private static News toNews(final Article article){

        // Protection: author
        if(article.getAuthor() == null || article.getAuthor().length() < 3){
            article.setAuthor("**[No Author]**");
        }
        // Protection: title
        if(article.getTitle() == null || article.getTitle().length() < 3){
            article.setTitle("**[No Tittle]**");
        }
        // Protection: description
        if(article.getDescription() == null || article.getDescription().length() < 3){
            article.setDescription("**[No Description]**");
        }


        ZonedDateTime publishedAt = ZonedDateTime
                .parse(article.getPublishedAt())
                // Correct from UTC to LocalTime (Chile summer);
                .withZoneSameInstant(ZoneId.of("-3"));

        // Construct News from Article
        return new News(
                article.getTitle(),
                article.getSource().getName(),
                article.getAuthor(),
                article.getUrl(),
                article.getUrlToImage(),
                article.getDescription(),
                article.getDescription(),
                publishedAt
        );
    }
    /**
    Return one new given its id.
     */
    @GetMapping("v1/news/{id}")
    public News one(@PathVariable final Long id){
        // FIXME: Change the RuntimeException to 404
        return this.newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News Not Found :("));
    }
}
