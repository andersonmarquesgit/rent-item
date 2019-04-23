package com.rentitem.restapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rentitem.restapi.api.entity.User;
import com.rentitem.restapi.api.enums.ProfileEnum;
import com.rentitem.restapi.api.repository.UserRepository;

@SpringBootApplication
public class RentitemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentitemApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	// Criando usuário ao iniciar a aplicação
	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, passwordEncoder);
		};
	}

	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		User admin = new User();
		admin.setEmail("admin@rentitem.com");
		admin.setPassword(passwordEncoder.encode("123456"));
		admin.setProfile(ProfileEnum.ROLE_ADMIN);

		User userTechnician = new User();
		userTechnician.setEmail("sc@rentitem.com");
		userTechnician.setPassword(passwordEncoder.encode("123456"));
		userTechnician.setProfile(ProfileEnum.ROLE_TECHNICIAN);


		User find = userRepository.findByEmail("admin@rentitem.com");
		User find1 = userRepository.findByEmail("sc@rentitem.com");
		User find2 = userRepository.findByEmail("fc@rentitem.com");
		if (find == null && find1 == null && find2 == null) {
			userRepository.save(admin);
			userRepository.save(userTechnician);
		}
	}

}
