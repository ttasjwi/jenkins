package com.ttasjwi.jenkins.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

    private val version = 0.1

    @GetMapping("/")
    fun home(): String {
        return "hello, this is spring boot server! (ver = $version)"
    }
}
