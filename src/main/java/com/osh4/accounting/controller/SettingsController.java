package com.osh4.accounting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.osh4.accounting.dto.SettingsDto;
import com.osh4.accounting.service.SettingsService;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private static final String SETTINGS_VIEW = "settings";
    private static final String SUCCESS = "SUCCESS";
    private final SettingsService settingsService;

    @Autowired
    public SettingsController(final SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public String getSettings(Model model) {
        model.addAttribute("settings", settingsService.getAllSettings());
        model.addAttribute("types", settingsService.getAllTypes());
        return SETTINGS_VIEW;
    }

    @ResponseBody
    @GetMapping("/async")
    public ResponseEntity<List<SettingsDto>> getSettingsAjax() {
        return ResponseEntity.ok(settingsService.getAllSettings());
    }

    @ResponseBody
    @PostMapping(value = "/add")
    public ResponseEntity<String> createSetting(@RequestBody SettingsDto settingsDto) {
        try {
            settingsService.create(settingsDto);
            return ResponseEntity.ok(SUCCESS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> updateSetting(@RequestBody SettingsDto settingsDto) {
        try {
            SettingsDto result = settingsService.update(settingsDto);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @ResponseBody
    @DeleteMapping
    public ResponseEntity<String> deleteSetting(@RequestBody SettingsDto settingsDto) {
        try {
            settingsService.delete(settingsDto);
            return ResponseEntity.ok(SUCCESS);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
