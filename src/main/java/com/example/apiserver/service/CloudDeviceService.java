package com.example.apiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apiserver.model.CloudDevice;
import com.example.apiserver.repository.CloudDeviceRepository;


import jakarta.transaction.Transactional;

@Service
@Transactional
public class CloudDeviceService {
    @Autowired
    private CloudDeviceRepository clouddeviceRepository;

    public Long register(CloudDevice clouddevice) {
        return clouddeviceRepository.save(clouddevice).getId(); 
    }

    public void unregister(CloudDevice clouddevice) {
        clouddeviceRepository.delete(clouddevice);
    }

    public Boolean isregisterBydeviceid(String deviceid) {
        return clouddeviceRepository.findByDeviceid(deviceid).isPresent();
    }

    public Boolean isregister(Long id, String deviceid) {        
        return clouddeviceRepository.findByIdAndDeviceid(id, deviceid).isPresent();
    }

}
