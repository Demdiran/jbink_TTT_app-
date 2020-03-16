package nl.sogyo.ttt_app.api;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nl.sogyo.ttt_app.domain.*;

@SpringBootApplication
@RestController
public class TTT_Application extends WebSecurityConfigurerAdapter{

	@GetMapping("/user")
	public Player user(@AuthenticationPrincipal OAuth2User principal) {
		String outside_ID = principal.getName();
		DatabaseAccessor databaseAccessor = new DatabaseAccessor();
		Player user = databaseAccessor.getOrCreatePlayerWithOutsideID(outside_ID);
		databaseAccessor.closeSession();
		return user;
	}

	@GetMapping("/tournaments")
	public List<Tournament> tournaments(){
		DatabaseAccessor databaseAccessor = new DatabaseAccessor();
		List<Tournament> response = databaseAccessor.getAllFromDB(Tournament.class);
		databaseAccessor.closeSession();
		return response;
	}

	@PostMapping("/createTournament")
	public Tournament createTournament(@AuthenticationPrincipal OAuth2User principal, @RequestBody Tournament tournament){
		DatabaseAccessor databaseAccessor = new DatabaseAccessor();
		System.out.println(tournament.getTournamentDate());
		databaseAccessor.createInDB(tournament);
		databaseAccessor.closeSession();
		return tournament;
	}

	@PostMapping("/editprofile")
	public void editprofile(@AuthenticationPrincipal OAuth2User principal, @RequestBody Player user){
		DatabaseAccessor databaseAccessor = new DatabaseAccessor();
		databaseAccessor.updateInDB(user);
		databaseAccessor.closeSession();
	}

	public static void main(String[] args) {
		SpringApplication.run(TTT_Application.class, args);
	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	// @formatter:off
        http
            .authorizeRequests(a -> a
                .antMatchers("/", "/error", "/indexStyle.css", "/tournaments").permitAll()
                .anyRequest().authenticated()
            )
			.logout(l -> l
				.logoutSuccessUrl("/").permitAll()
			)
			.csrf(c -> c
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			)
            .exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .oauth2Login();
        // @formatter:on
    }

}
