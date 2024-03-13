package com.in.ecommerce.controller;

import com.in.ecommerce.daoImpl.UserDaoImpl;
import com.in.ecommerce.dto.AuthenticationRequest;
import com.in.ecommerce.dto.SignupRequest;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.service.AuthService;
import com.in.ecommerce.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final String HEADER_STRING ="Authorization";
    private static final String TOKEN_PREFIX ="Bearer ";

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final com.in.ecommerce.service.UserDetailsService userDetailsServiceCustom;
    private final AuthService authService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        }catch (BadCredentialsException ex){
            log.info("Bad Credentials");
            throw new BadCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser=userDetailsServiceCustom.findByUsernameEmail(authenticationRequest.getUsername());
        final String jwt=jwtUtil.generateToken(authenticationRequest.getUsername());

        if(optionalUser.isPresent()){
            response.getWriter().write(new JSONObject()
                    .put("userId",optionalUser.get().getId())
                    .put("role",optionalUser.get().getRole())
                    .toString()
            );

            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow-Headers","Authorization, X-PINGOTHER, Origin,"+
                    "X-Requested-With,Content-Type,Accept,X-Custom-header");
            response.addHeader(HEADER_STRING,TOKEN_PREFIX + jwt);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
         if(authService.hasUserWithEmail(signupRequest.getEmail())){
             return new ResponseEntity<>("User already exists", HttpStatus.NOT_ACCEPTABLE);
         }
         User user=authService.createUser(signupRequest);
         return new ResponseEntity<>(user,HttpStatus.OK);
    }



}

