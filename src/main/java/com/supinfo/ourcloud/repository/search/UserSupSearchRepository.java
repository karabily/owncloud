package com.supinfo.ourcloud.repository.search;

import com.supinfo.ourcloud.domain.UserSup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the UserSup entity.
 */
public interface UserSupSearchRepository extends ElasticsearchRepository<UserSup, Long> {
}
