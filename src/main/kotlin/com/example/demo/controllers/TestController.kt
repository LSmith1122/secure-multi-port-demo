package com.example.demo.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
class TestController {

    @RequestMapping("/test", method = [RequestMethod.POST])
    fun handleWebhookResponse(): ResponseEntity<String?> {
        println("Successful Response")
        return ResponseEntity(HttpStatus.OK)
    }

}