package com.supinfo.ourcloud.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.supinfo.ourcloud.domain.UserSup;

import com.supinfo.ourcloud.repository.UserSupRepository;
import com.supinfo.ourcloud.repository.search.UserSupSearchRepository;
import com.supinfo.ourcloud.web.rest.errors.BadRequestAlertException;
import com.supinfo.ourcloud.web.rest.util.HeaderUtil;
import com.supinfo.ourcloud.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UserSup.
 */
@RestController
@RequestMapping("/api")
public class UserSupResource {

    private final Logger log = LoggerFactory.getLogger(UserSupResource.class);

    private static final String ENTITY_NAME = "userSup";

    private final UserSupRepository userSupRepository;

    private final UserSupSearchRepository userSupSearchRepository;

    public UserSupResource(UserSupRepository userSupRepository, UserSupSearchRepository userSupSearchRepository) {
        this.userSupRepository = userSupRepository;
        this.userSupSearchRepository = userSupSearchRepository;
    }

    /**
     * POST  /user-sups : Create a new userSup.
     *
     * @param userSup the userSup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userSup, or with status 400 (Bad Request) if the userSup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-sups")
    @Timed
    public ResponseEntity<UserSup> createUserSup(@RequestBody UserSup userSup) throws URISyntaxException {
        log.debug("REST request to save UserSup : {}", userSup);
        if (userSup.getId() != null) {
            throw new BadRequestAlertException("A new userSup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserSup result = userSupRepository.save(userSup);
        userSupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-sups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-sups : Updates an existing userSup.
     *
     * @param userSup the userSup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userSup,
     * or with status 400 (Bad Request) if the userSup is not valid,
     * or with status 500 (Internal Server Error) if the userSup couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-sups")
    @Timed
    public ResponseEntity<UserSup> updateUserSup(@RequestBody UserSup userSup) throws URISyntaxException {
        log.debug("REST request to update UserSup : {}", userSup);
        if (userSup.getId() == null) {
            return createUserSup(userSup);
        }
        UserSup result = userSupRepository.save(userSup);
        userSupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userSup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-sups : get all the userSups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userSups in body
     */
    @GetMapping("/user-sups")
    @Timed
    public ResponseEntity<List<UserSup>> getAllUserSups(Pageable pageable) {
        log.debug("REST request to get a page of UserSups");
        Page<UserSup> page = userSupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-sups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-sups/:id : get the "id" userSup.
     *
     * @param id the id of the userSup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userSup, or with status 404 (Not Found)
     */
    @GetMapping("/user-sups/{id}")
    @Timed
    public ResponseEntity<UserSup> getUserSup(@PathVariable Long id) {
        log.debug("REST request to get UserSup : {}", id);
        UserSup userSup = userSupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userSup));
    }

    /**
     * DELETE  /user-sups/:id : delete the "id" userSup.
     *
     * @param id the id of the userSup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-sups/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserSup(@PathVariable Long id) {
        log.debug("REST request to delete UserSup : {}", id);
        userSupRepository.delete(id);
        userSupSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-sups?query=:query : search for the userSup corresponding
     * to the query.
     *
     * @param query the query of the userSup search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/user-sups")
    @Timed
    public ResponseEntity<List<UserSup>> searchUserSups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UserSups for query {}", query);
        Page<UserSup> page = userSupSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-sups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
