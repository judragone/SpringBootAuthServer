package com.example.apiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apiserver.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Boolean deviceidauth(String deviceid) {        
        return productRepository.findByDeviceid(deviceid).isPresent();
    }    
}
