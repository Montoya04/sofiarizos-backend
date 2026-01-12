package com.sofiarizos.backend.service;

import com.sofiarizos.backend.model.Masterclass;
import com.sofiarizos.backend.repository.MasterclassRepository;
import org.springframework.stereotype.Service;

@Service
public class MasterclassService {

    private final MasterclassRepository repository;

    public MasterclassService(MasterclassRepository repository) {
        this.repository = repository;
    }

    public Masterclass guardar(Masterclass masterclass) {
        return repository.save(masterclass);
    }
}
