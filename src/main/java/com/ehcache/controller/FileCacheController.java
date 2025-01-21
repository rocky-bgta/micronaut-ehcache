package com.ehcache.controller;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller("/file")
public class FileCacheController {

    private static final Logger LOG = LoggerFactory.getLogger(FileCacheController.class);

    @Inject
    private Cache<String, byte[]> defaultCache;

    private static final String FILE_KEY = "resume-pdf";
    private static final String FILE_PATH = "C:/Git-Repo/eh-micronut/eh-cache-ide/input/Resume_Nazmus_Salahin.pdf";

    @Get(value = "/resume", produces = MediaType.APPLICATION_PDF)
    public HttpResponse<byte[]> getResume() {
        try {
            // Check if the file content is already cached
            Optional<byte[]> cachedContent = Optional.ofNullable(defaultCache.get(FILE_KEY));

            if (cachedContent.isPresent()) {
                LOG.info("Serving file from cache");
                return HttpResponse.ok(cachedContent.get());
            }

            // If not cached, read the file, cache it, and return the content
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return HttpResponse.notFound();
            }

            byte[] fileContent = FileUtils.readFileToByteArray(file);

            // Cache the file content
            defaultCache.put(FILE_KEY, fileContent);

            LOG.info("File cached successfully");
            return HttpResponse.ok(fileContent);

        } catch (IOException e) {
            LOG.error("Error reading or caching the file: ", e);
            //return HttpResponse.serverError("Unable to read the file");
        }
        return HttpResponse.serverError("Unable to read the file".getBytes());
    }
}
