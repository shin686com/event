package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {



    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    int pwChange(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO idFinder(UserInfoDTO pDTO) throws Exception;

    int delId(UserInfoDTO pDTO) throws Exception;

    int getUserLogin(UserInfoDTO pDTO) throws Exception;
}

