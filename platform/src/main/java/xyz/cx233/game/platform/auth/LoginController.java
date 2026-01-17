package xyz.cx233.game.platform.auth;

import org.springframework.web.bind.annotation.*;
import xyz.cx233.game.platform.auth.model.UserDto;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResult login(@RequestBody UserDto userDto) {
        return authService.login(userDto);
    }

}
