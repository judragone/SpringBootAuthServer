package com.example.apiserver.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.example.apiserver.exception.CustomException;
import com.example.apiserver.model.CloudDevice;
import com.example.apiserver.model.Log;
import com.example.apiserver.service.CloudDeviceService;
import com.example.apiserver.service.LogService;
import com.example.apiserver.service.ProductService;
import com.example.apiserver.service.ResponseService;
import com.example.apiserver.exception.ErrorCode;
import com.example.apiserver.token.JwtProvider;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
public class DeviceController {
    @Autowired
    private ProductService productService;       
    @Autowired
    private CloudDeviceService clouddeviceService;
    @Autowired
    private LogService logService;

    private final JwtProvider jwtProvider;
    private final ResponseService resService;
    
    @ResponseBody
    @RequestMapping(value="/Device/Register", method = RequestMethod.POST)
    public Object DeviceRegister(@RequestBody Map<String, Object> requestData) {      
        if(false == requestData.containsKey("deviceid"))
            throw new CustomException(ErrorCode.INVALID_PARAM);
                
        String deviceid = (String) requestData.get("deviceid");
        
        if(false == productService.deviceidauth(deviceid))
            throw new CustomException(ErrorCode.INVALID_AUTH);
        
        if(true == clouddeviceService.isregisterBydeviceid(deviceid))
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        
        
        CloudDevice cloudDevice = new CloudDevice();
        cloudDevice.setDeviceid(deviceid);

        JsonObject result = new JsonObject();
        result.addProperty("id", clouddeviceService.register(cloudDevice));
        return resService.getResult(result);
    }

    @ResponseBody
    @RequestMapping(value="/Device/Auth", method = RequestMethod.POST)
    public Object DeviceAuth(@RequestBody Map<String, Object> requestData) {          
        if(false == requestData.containsKey("id") || false == requestData.containsKey("deviceid"))
            throw new CustomException(ErrorCode.INVALID_PARAM);
                  
        Long id = Long.parseLong(requestData.get("id").toString());
        String deviceid = (String) requestData.get("deviceid");

        if(false == clouddeviceService.isregister(id, deviceid))
            throw new CustomException(ErrorCode.INVALID_AUTH);

        JsonObject result = new JsonObject();
        result.addProperty("token", jwtProvider.createToken(id.toString()));
        return resService.getResult(result);       
    }

    @ResponseBody
    @RequestMapping(value="/Device/Log", method = RequestMethod.POST)
    public Object DeviceLog(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {        
        if(false == requestData.containsKey("deviceid"))
            throw new CustomException(ErrorCode.INVALID_PARAM);

        if(false == jwtProvider.checkToken(request))
            throw new CustomException(ErrorCode.INVALID_AUTH);
         
        Long idx = jwtProvider.getUserPk(request);
        System.out.println("user pk : "+jwtProvider.getUserPk(request));

        String deviceid = (String) requestData.get("deviceid");
        String eventtype = (String) requestData.get("eventtype");
        String message = (String) requestData.get("message");
        
        Log log = new Log();
        log.setDeviceid(deviceid);
        log.setEventtype(eventtype);
        log.setMessage(message);

        logService.saveLog(log);        
        return resService.getResult(null);     
    }
}
