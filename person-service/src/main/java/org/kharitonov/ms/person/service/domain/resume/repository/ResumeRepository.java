package org.kharitonov.ms.person.service.domain.resume.repository;

import org.kharitonov.ms.person.service.domain.resume.entity.Resume;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends ElasticsearchRepository<Resume, Long>{

}