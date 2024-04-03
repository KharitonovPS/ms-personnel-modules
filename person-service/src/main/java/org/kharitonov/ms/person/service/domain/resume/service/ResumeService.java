package org.kharitonov.ms.person.service.domain.resume.service;

import lombok.RequiredArgsConstructor;
import org.kharitonov.ms.person.service.domain.resume.entity.Resume;
import org.kharitonov.ms.person.service.domain.resume.repository.ResumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public List<Resume> getResumes(){
        return StreamSupport.stream(resumeRepository.findAll().spliterator(), false)
                .toList();
    }
}
