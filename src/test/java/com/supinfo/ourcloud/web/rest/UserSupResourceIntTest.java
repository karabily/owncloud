package com.supinfo.ourcloud.web.rest;

import com.supinfo.ourcloud.OurcloudApp;

import com.supinfo.ourcloud.domain.UserSup;
import com.supinfo.ourcloud.repository.UserSupRepository;
import com.supinfo.ourcloud.repository.search.UserSupSearchRepository;
import com.supinfo.ourcloud.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.supinfo.ourcloud.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserSupResource REST controller.
 *
 * @see UserSupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OurcloudApp.class)
public class UserSupResourceIntTest {

    @Autowired
    private UserSupRepository userSupRepository;

    @Autowired
    private UserSupSearchRepository userSupSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserSupMockMvc;

    private UserSup userSup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserSupResource userSupResource = new UserSupResource(userSupRepository, userSupSearchRepository);
        this.restUserSupMockMvc = MockMvcBuilders.standaloneSetup(userSupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSup createEntity(EntityManager em) {
        UserSup userSup = new UserSup();
        return userSup;
    }

    @Before
    public void initTest() {
        userSupSearchRepository.deleteAll();
        userSup = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserSup() throws Exception {
        int databaseSizeBeforeCreate = userSupRepository.findAll().size();

        // Create the UserSup
        restUserSupMockMvc.perform(post("/api/user-sups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSup)))
            .andExpect(status().isCreated());

        // Validate the UserSup in the database
        List<UserSup> userSupList = userSupRepository.findAll();
        assertThat(userSupList).hasSize(databaseSizeBeforeCreate + 1);
        UserSup testUserSup = userSupList.get(userSupList.size() - 1);

        // Validate the UserSup in Elasticsearch
        UserSup userSupEs = userSupSearchRepository.findOne(testUserSup.getId());
        assertThat(userSupEs).isEqualToIgnoringGivenFields(testUserSup);
    }

    @Test
    @Transactional
    public void createUserSupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userSupRepository.findAll().size();

        // Create the UserSup with an existing ID
        userSup.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSupMockMvc.perform(post("/api/user-sups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSup)))
            .andExpect(status().isBadRequest());

        // Validate the UserSup in the database
        List<UserSup> userSupList = userSupRepository.findAll();
        assertThat(userSupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserSups() throws Exception {
        // Initialize the database
        userSupRepository.saveAndFlush(userSup);

        // Get all the userSupList
        restUserSupMockMvc.perform(get("/api/user-sups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSup.getId().intValue())));
    }

    @Test
    @Transactional
    public void getUserSup() throws Exception {
        // Initialize the database
        userSupRepository.saveAndFlush(userSup);

        // Get the userSup
        restUserSupMockMvc.perform(get("/api/user-sups/{id}", userSup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userSup.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserSup() throws Exception {
        // Get the userSup
        restUserSupMockMvc.perform(get("/api/user-sups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserSup() throws Exception {
        // Initialize the database
        userSupRepository.saveAndFlush(userSup);
        userSupSearchRepository.save(userSup);
        int databaseSizeBeforeUpdate = userSupRepository.findAll().size();

        // Update the userSup
        UserSup updatedUserSup = userSupRepository.findOne(userSup.getId());
        // Disconnect from session so that the updates on updatedUserSup are not directly saved in db
        em.detach(updatedUserSup);

        restUserSupMockMvc.perform(put("/api/user-sups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserSup)))
            .andExpect(status().isOk());

        // Validate the UserSup in the database
        List<UserSup> userSupList = userSupRepository.findAll();
        assertThat(userSupList).hasSize(databaseSizeBeforeUpdate);
        UserSup testUserSup = userSupList.get(userSupList.size() - 1);

        // Validate the UserSup in Elasticsearch
        UserSup userSupEs = userSupSearchRepository.findOne(testUserSup.getId());
        assertThat(userSupEs).isEqualToIgnoringGivenFields(testUserSup);
    }

    @Test
    @Transactional
    public void updateNonExistingUserSup() throws Exception {
        int databaseSizeBeforeUpdate = userSupRepository.findAll().size();

        // Create the UserSup

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserSupMockMvc.perform(put("/api/user-sups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userSup)))
            .andExpect(status().isCreated());

        // Validate the UserSup in the database
        List<UserSup> userSupList = userSupRepository.findAll();
        assertThat(userSupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserSup() throws Exception {
        // Initialize the database
        userSupRepository.saveAndFlush(userSup);
        userSupSearchRepository.save(userSup);
        int databaseSizeBeforeDelete = userSupRepository.findAll().size();

        // Get the userSup
        restUserSupMockMvc.perform(delete("/api/user-sups/{id}", userSup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userSupExistsInEs = userSupSearchRepository.exists(userSup.getId());
        assertThat(userSupExistsInEs).isFalse();

        // Validate the database is empty
        List<UserSup> userSupList = userSupRepository.findAll();
        assertThat(userSupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserSup() throws Exception {
        // Initialize the database
        userSupRepository.saveAndFlush(userSup);
        userSupSearchRepository.save(userSup);

        // Search the userSup
        restUserSupMockMvc.perform(get("/api/_search/user-sups?query=id:" + userSup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSup.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSup.class);
        UserSup userSup1 = new UserSup();
        userSup1.setId(1L);
        UserSup userSup2 = new UserSup();
        userSup2.setId(userSup1.getId());
        assertThat(userSup1).isEqualTo(userSup2);
        userSup2.setId(2L);
        assertThat(userSup1).isNotEqualTo(userSup2);
        userSup1.setId(null);
        assertThat(userSup1).isNotEqualTo(userSup2);
    }
}
