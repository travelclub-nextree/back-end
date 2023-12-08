package com.example.backend.controller;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.entity.Member;
import com.example.backend.jwt.JwtTokenProvider;
import com.example.backend.service.MemberService;
import com.example.backend.util.NoSuchEmailException;
import com.example.backend.util.PasswordNotMatchException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            Member member = memberService.login(loginDTO);
            String token = jwtTokenProvider.create(member.getMemberEmail(), member.getMemberId());

            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "Login Succeed.");
            returnMap.put("token", token);
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (NoSuchEmailException | PasswordNotMatchException e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            Map<String, String> returnMap = new HashMap<>();
            returnMap.put("message", "Logout Succeed.");
            responseDTO.setItem(returnMap);
            responseDTO.setStatusCode(HttpStatus.OK.value());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            responseDTO.setErrorMessage(e.getMessage());
            responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
