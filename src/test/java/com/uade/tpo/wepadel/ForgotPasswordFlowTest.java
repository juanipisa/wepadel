package com.uade.tpo.wepadel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.uade.tpo.wepadel.controllers.auth.ForgotPasswordRequest;
import com.uade.tpo.wepadel.controllers.auth.RegisterRequest;
import com.uade.tpo.wepadel.entity.RolEnum;
import com.uade.tpo.wepadel.service.AuthenticationService;

@SpringBootTest
class ForgotPasswordFlowTest {

  @Autowired
  private AuthenticationService authenticationService;

  @Test
  void forgotPasswordTwiceForSameUserDoesNotThrow() {
    var email = "forgot-flow-test-" + System.currentTimeMillis() + "@wepadel.com";

    authenticationService.register(RegisterRequest.builder()
        .nombreApellido("Forgot Flow Test")
        .email(email)
        .password("TestPass123!@")
        .role(RolEnum.CLIENTE)
        .build());

    var request = ForgotPasswordRequest.builder().email(email).build();

    var first = assertDoesNotThrow(() -> authenticationService.forgotPassword(request));
    assertNotNull(first.getToken());

    assertDoesNotThrow(() -> authenticationService.forgotPassword(request));
  }
}
