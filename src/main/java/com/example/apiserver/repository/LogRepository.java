package com.example.apiserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.apiserver.model.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
}