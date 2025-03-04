package com.example.urlshortner.web.controller;

import com.example.urlshortner.core.UrlManager;
//import com.example.urlshortner.core.statistics.UrlStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    private final UrlManager urlManager;

    @Autowired
    public RedirectController(UrlManager urlManager) {
        this.urlManager = urlManager;
    }

    @GetMapping("/{shortCode}")
    public RedirectView redirectToOriginalUrl(@PathVariable String shortCode) {
        String longUrl = urlManager.expandUrl(shortCode);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(longUrl);
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);

        return redirectView;
    }
}
