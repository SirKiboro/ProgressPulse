package com.progresspulse.app;

import com.progresspulse.app.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.progresspulse.app")
@EnableJpaAuditing
@Import(SecurityConfig.class)
public class ProgressPulseApplication {

	public static void main(String[] args) {

        SpringApplication.run(ProgressPulseApplication.class, args);
	}

}
