package com.timvero.example.admin.risk.github;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.timvero.loan.risk.datasource.DataUnavaliableException;
import com.timvero.loan.risk.datasource.MappedDataSource;
import java.io.IOException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service(GithubDataSource.DATASOURCE_NAME)
public class GithubDataSource implements MappedDataSource<GithubDataSourceSubject, GithubUser> {
    public static final String DATASOURCE_NAME = "github";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String GITHUB_API_BASE_URL = "https://api.github.com";

    {
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github.v3+json");
        return new HttpEntity<>(headers);
    }

    @Override
    public Class<GithubUser> getType() {
        return GithubUser.class;
    }

    @Override
    public Content getData(GithubDataSourceSubject subject) throws Exception {
        try {
            String url = GITHUB_API_BASE_URL + "/users/" + subject.getGithubUsername();
            ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHttpEntity(),
                byte[].class
            );
            return new Content(response.getBody(), MediaType.APPLICATION_JSON_VALUE) ;
        } catch (HttpClientErrorException.NotFound e) {
            throw new DataUnavaliableException("User not found: " + subject.getGithubUsername());
        }
    }

    @Override
    public GithubUser parseRecord(Content data) throws IOException {
        return objectMapper.readValue(data.getBody(), GithubUser.class);
    }
}
