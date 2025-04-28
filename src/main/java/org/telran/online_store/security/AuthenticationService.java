package org.telran.online_store.security;

public interface AuthenticationService {

    SignInResponse authenticate(SignInRequest request);
}
