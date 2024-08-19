package spring.security.Service;

import org.springframework.stereotype.Service;
import spring.security.Model.User;
import spring.security.dto.LoginDto;
import spring.security.dto.RegisterDto;

public interface AuthService {

    User registerUser(RegisterDto userInput);
    User authenicateUser(LoginDto userInput);
}
