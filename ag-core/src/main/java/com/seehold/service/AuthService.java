package com.seehold.service;

import com.seehold.dto.LoginDTO;
import com.seehold.dto.RegisterDTO;
import com.seehold.vo.LoginVO;
import com.seehold.vo.UserVO;

public interface AuthService {

    LoginVO login(LoginDTO loginDTO);

    LoginVO refreshToken(String refreshToken);

    UserVO register(RegisterDTO registerDTO);

    void logout(Long userId);
}