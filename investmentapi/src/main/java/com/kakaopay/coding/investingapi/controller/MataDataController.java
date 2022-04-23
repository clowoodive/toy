package com.kakaopay.coding.investingapi.controller;

import com.kakaopay.coding.investingapi.service.MetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
public class MataDataController {
    private final MetaDataService metaDataService;

    @Autowired
    public MataDataController(MetaDataService metaDataService) {
        this.metaDataService = metaDataService;
    }

    @PostMapping("/init")
    public void initMetaData() {
        metaDataService.initMetaData();
    }
}
