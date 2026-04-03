package com.seehold.service;

import com.seehold.dto.UserDTO;
import com.seehold.vo.PageVO;
import com.seehold.vo.UserVO;

import java.util.List;

public interface UserService {

    PageVO<UserVO> getUserList(Long current, Long size, String keyword);

    UserVO getUserById(Long id);

    UserVO createUser(UserDTO userDTO);

    UserVO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);

    void assignRoles(Long userId, List<Long> roleIds, Long operatorId);

    void updateStatus(Long id, Integer status);
}