package controller;

import http.common.HttpHeader;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpStatus;
import http.response.Response200;
import http.response.ResponseBody;
import http.response.ResponseBodyParser;
import http.response.StatusLine;
import org.apache.tika.Tika;

public class ResourceController implements Controller {

    private static final String RESOURCE_FILE_REGEX = "^.+\\.([a-zA-Z]+)$";

    @Override
    public HttpResponse service(final HttpRequest httpRequest) {
        StatusLine statusLine = new StatusLine(httpRequest.getHttpVersion(), HttpStatus.OK);

        String filePath = httpRequest.findPathPrefix() + httpRequest.getPath();
        ResponseBody responseBody = ResponseBodyParser.parse(filePath);

        String contentType = new Tika().detect(filePath);
        HttpHeader responseHeader = new HttpHeader();
        responseHeader.putHeader("Content-Type", contentType);
        responseHeader.putHeader("Content-Length", Integer.toString(responseBody.getLength()));
        return new Response200(statusLine, responseHeader, responseBody);
    }

    @Override
    public boolean isMapping(final RequestMapping requestMapping) {
        return requestMapping.isMatches(RESOURCE_FILE_REGEX);
    }
}