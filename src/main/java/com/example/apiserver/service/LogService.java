package com.example.apiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apiserver.model.Log;
import com.example.apiserver.repository.LogRepository;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void saveLog(Log log) {
        logRepository.save(log);
    }    
}
