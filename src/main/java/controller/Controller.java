package controller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface Controller {

    HttpResponse service(HttpRequest httpRequest);

    boolean isMapping(RequestMapping requestMapping);
}