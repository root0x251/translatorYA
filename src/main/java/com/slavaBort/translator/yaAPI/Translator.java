package com.slavaBort.translator.yaAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Vyacheslav Alekseevich
 * 19/02/2020
 */

public class Translator {

    private String YA_KEY = "trnsl.1.1.20191205T080823Z.bb65c6959adee4c4.a52d173186b595d60cb30c4222c79fef09d6c904";
    private String LANGUAGE_DICT_BODY_REQUEST = "https://translate.yandex.net/api/v1.5/tr.json/getLangs";
    private String TRANSLATE_BODY_REQUEST = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    public Map<String, String> checkDictionary() {
        Map<String, String> langMap = new HashMap<>();
        langMap.put("key", YA_KEY);
        langMap.put("ui", "ru");
        // return json
        JSONObject json = requestToYandex(LANGUAGE_DICT_BODY_REQUEST, langMap);
        // convert to map
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json.get("langs").toString(), Map.class);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String translateText(String text, String langFrom, String langTo) {
        Map<String, String> translateTextMap = new HashMap<>();
        translateTextMap.put("key", YA_KEY);
        translateTextMap.put("text", text);
        if (!langFrom.equals("") && !langFrom.isEmpty()) {
            translateTextMap.put("lang", langFrom + "-" + langTo);
        } else {
            translateTextMap.put("lang", langTo);
        }
        // return json
        JSONObject json = requestToYandex(TRANSLATE_BODY_REQUEST, translateTextMap);
        JSONArray array = null;
        try {
            array = json.getJSONArray("text");
            return array.get(0).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }

    private JSONObject requestToYandex(String site, Map<String, String> paramRequest) {
        try {
            URIBuilder url = new URIBuilder(site);
            paramRequest.forEach(url::addParameter);

            HttpGet getRequest = new HttpGet(url.toString());
            getRequest.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU");
            getRequest.addHeader(HttpHeaders.ACCEPT_ENCODING, "utf-8");

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(getRequest)) {
                int statusCode = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                if (statusCode == 200) {
                    if (entity != null) {
                        String result = EntityUtils.toString(entity);
                        return new JSONObject(result);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
