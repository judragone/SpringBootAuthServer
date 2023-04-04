package com.example.apiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiserver.model.CloudDevice;

@Repository
public interface CloudDeviceRepository extends JpaRepository<CloudDevice, Long> {
    Optional<CloudDevice> findByDeviceid(String deviceid);
    Optional<CloudDevice> findByIdAndDeviceid(Long id, String deviceid);    
}