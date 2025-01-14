package com.example.canvasintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@SpringBootApplication
public class CanvasintegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanvasintegrationApplication.class, args);
	}

	// Default controller to handle root URL ("/")
	@RestController
	public class DefaultController {
		@GetMapping("/")
		public String home() {
			return "Welcome to the Canvas Integration API!";
		}
	}

	// Global exception handler to catch 404 (Not Found) errors
	@ControllerAdvice
	public class FallbackController {
		@ExceptionHandler(NoHandlerFoundException.class)
		@ResponseBody
		@ResponseStatus(HttpStatus.NOT_FOUND)
		public String handleNotFound() {
			return "The requested resource was not found. Please check the URL and try again.";
		}
	}
}
