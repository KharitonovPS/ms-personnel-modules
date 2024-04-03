package org.kharitonov.ms.person.service.domain.resume.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "resume_text")
public class Resume {

    //postman создать индекс
    //записать JSON
    //дернуть с эластика
    //https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html

    private Long id;
    private String title;
    private String text;
    private Long personId;
}
