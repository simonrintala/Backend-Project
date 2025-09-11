package com.Java24GroupProject.AirBnBPlatform.services.AuthenticationAndValidation;

import com.Java24GroupProject.AirBnBPlatform.models.User;

/************************
 * IAuthenticationService
 * ---
 * this interface allows other classes to use the AuthenticationService class methods
 * without having an instance of the AuthenticationService class, allowing looser coupling
 * between the different service classes
 ***********************/
public interface IAuthenticationService {

    default User authenticateAndExtractUser() {return null;}
}
