package com.viettel.project.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.viettel.project.service.AuthorityService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

@RestController
public class SwaggerController {
    private final Logger logger = LoggerFactory.getLogger(SwaggerController.class);

    @Autowired
    private AuthorityService authorityService;

    @GetMapping(value = "/sapis")
    public void getAllSwaggerPaths(){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(createHeaders("quanzip", "123"));

//        body la 1 mang cac thuoc tinh Xs(contain paths), paths bao gom cac apis, cac api gom mang cac methods
//                      paths               apis                  methods
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> body;
        ResponseEntity<Object> responseEntity = restTemplate.exchange("http://localhost:9081/sw-api", HttpMethod.GET, httpEntity, Object.class);
        body =(LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>>) responseEntity.getBody();

        if(body.containsKey("paths")){
            LinkedHashMap<String, LinkedHashMap<String,  Object>> apis = body.get("paths");
            for(Map.Entry<String, LinkedHashMap<String, Object>> api: apis.entrySet()){
                String apiPath = api.getKey();
                for(Map.Entry<String, Object> method: api.getValue().entrySet()){
                    String methodName =  method.getKey();
                    importPathsAndRoles(apiPath, methodName);
                }
            }
        }

    }

    // call Api with basic authen
   private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

    private void importPathsAndRoles(String paths, String methodName){
        final String postMethod = "post";
        String roleAdmin = "ROLE_ADMIN";
        String roleUser = "ROLE_USER";

        paths = paths.toLowerCase(Locale.ROOT);
        methodName = methodName.toLowerCase(Locale.ROOT);

        // get first ele in paths /*/*/*
        String path = paths.split("\\/")[1].toLowerCase();
        if(path.equals("user") || path.equals("bill") || path.equals("product")) {
            switch (methodName){
                case postMethod: authorityService.saveAuthority(paths, postMethod, roleAdmin); break;
                default: authorityService.saveAuthority(paths, methodName, roleUser);
            }
        }else {
            authorityService.saveAuthority(paths, methodName, roleUser);
        }
    }
}
