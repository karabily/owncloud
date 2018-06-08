package com.supinfo.ourcloud.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.supinfo.ourcloud.domain.Job;

import com.supinfo.ourcloud.repository.JobRepository;
import com.supinfo.ourcloud.repository.search.JobSearchRepository;
import com.supinfo.ourcloud.web.rest.errors.BadRequestAlertException;
import com.supinfo.ourcloud.web.rest.util.HeaderUtil;
import com.supinfo.ourcloud.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Job.
 */
@RestController
@RequestMapping("/api")
public class JobResource {

    private final Logger log = LoggerFactory.getLogger(JobResource.class);

    private static final String ENTITY_NAME = "job";

    private final JobRepository jobRepository;

    private final JobSearchRepository jobSearchRepository;
    private static final int BUFFER_SIZE = 4096;
    private static final String HOST = "localhost";
    private static final String USER = "karabily";
    private static final String PASSWORD = "z9QwkAWR";
    private static final String FILEPATH = "c:/Users/Karabily/Desktop/Capture.png";
    private static final String UPLOADPATH = "Capture.png";
    private static String FTPURL = "ftp://%s:%s@%s/%s;type=i";

    public JobResource(JobRepository jobRepository, JobSearchRepository jobSearchRepository) {
        this.jobRepository = jobRepository;
        this.jobSearchRepository = jobSearchRepository;
    }

    /**
     * POST  /jobs : Create a new job.
     *
     * @param job the job to create
     * @return the ResponseEntity with status 201 (Created) and with body the new job, or with status 400 (Bad Request) if the job has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jobs")
    @Timed
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to save Job : {}", job);
        if (job.getId() != null) {
            throw new BadRequestAlertException("A new job cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Job result = jobRepository.save(job);
        System.out.println("Name:>> " + job.getUserToJob().getLogin());
        FTPURL = String.format(FTPURL, USER, PASSWORD, HOST, UPLOADPATH);
        System.out.println("Upload URL: " + FTPURL);

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(HOST, 21);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return null;
            }
            boolean success = ftpClient.login(USER, PASSWORD);
            showServerReply(ftpClient);
            if (!success) {
                System.out.println("Could not login to the server");
                return null;
            } else {
                System.out.println("great");

            }
            String dirToCreate = "/upload/" + job.getUserToJob().getEmail();
            success = ftpClient.makeDirectory(dirToCreate);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created directory: " + dirToCreate);
                upload(FTPURL);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
                upload(FTPURL);
            }
            // logs out
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        jobSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    private static void upload(String FTPURL)  {
        try {
            URL url = new URL(FTPURL);
            URLConnection conn = url.openConnection();
            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(FILEPATH);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            System.out.println("File uploaded");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * PUT  /jobs : Updates an existing job.
     *
     * @param job the job to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated job,
     * or with status 400 (Bad Request) if the job is not valid,
     * or with status 500 (Internal Server Error) if the job couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jobs")
    @Timed
    public ResponseEntity<Job> updateJob(@Valid @RequestBody Job job) throws URISyntaxException {
        log.debug("REST request to update Job : {}", job);
        if (job.getId() == null) {
            return createJob(job);
        }
        Job result = jobRepository.save(job);
        jobSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, job.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobs : get all the jobs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of jobs in body
     */
    @GetMapping("/jobs")
    @Timed
    public ResponseEntity<List<Job>> getAllJobs(Pageable pageable) {
        log.debug("REST request to get a page of Jobs");
        Page<Job> page = jobRepository.findAll(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /jobs/:id : get the "id" job.
     *
     * @param id the id of the job to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the job, or with status 404 (Not Found)
     */
    @GetMapping("/jobs/{id}")
    @Timed
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        log.debug("REST request to get Job : {}", id);
        Job job = jobRepository.findOne(id);
        System.out.println("Name:>> " + job.getUserToJob().getEmail());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(job));
    }

    /**
     * DELETE  /jobs/:id : delete the "id" job.
     *
     * @param id the id of the job to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jobs/{id}")
    @Timed
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        log.debug("REST request to delete Job : {}", id);
        jobRepository.delete(id);
        jobSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobs?query=:query : search for the job corresponding
     * to the query.
     *
     * @param query    the query of the job search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/jobs")
    @Timed
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Jobs for query {}", query);
        Page<Job> page = jobSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
