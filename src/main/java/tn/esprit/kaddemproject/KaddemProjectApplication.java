package tn.esprit.kaddemproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//
// @EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication

//@ComponentScan({"tn.esprit.kaddemproject.generic"})
//@EntityScan("tn.esprit.kaddemproject.generic.BaseRepository")
//@EnableJpaRepositories("tn.esprit.kaddemproject.generic")
public class KaddemProjectApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    public static void main(String[] args) {
        SpringApplication.run(KaddemProjectApplication.class, args);
    }

}
