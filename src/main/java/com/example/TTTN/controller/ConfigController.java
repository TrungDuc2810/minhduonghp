package com.example.TTTN.controller;

import com.example.TTTN.payload.ConfigDto;
import com.example.TTTN.payload.ListResponse;
import com.example.TTTN.service.ConfigService;
import com.example.TTTN.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configs")
public class ConfigController {
    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping
    public ResponseEntity<ConfigDto> addConfig(@RequestBody ConfigDto configDto) {
        return new ResponseEntity<>(configService.addConfig(configDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ListResponse<ConfigDto> getAllConfigs(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return configService.getAllConfigs(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/{field}")
    public ResponseEntity<ConfigDto> getConfig(@PathVariable(name = "field") String field) {
        return new ResponseEntity<>(configService.getConfig(field), HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('ADMIN_KD')")
    @PutMapping(value = "/{field}")
    public ResponseEntity<ConfigDto> updatePartner(@PathVariable(name = "field") String field,
                                                    @RequestBody ConfigDto configDto) {
        return new ResponseEntity<>(configService.updateConfig(configDto, field), HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('ADMIN_KD')")
    @DeleteMapping(value = "/{field}")
    public ResponseEntity<String> deletePartnerById(@PathVariable(name = "field") String field) {
        configService.deleteConfig(field);
        return new ResponseEntity<>("Delete successfully!!!", HttpStatus.OK);
    }
}
