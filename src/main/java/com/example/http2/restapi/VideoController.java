package com.example.http2.restapi;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.http2.service.VideoService;

@RestController
@RequestMapping("video")
public class VideoController {
    
    @Autowired
    private VideoService videos;

    @GetMapping
    public List<String> getAllVideos() throws IOException {
        return videos.getAllVideos();
    }

}
