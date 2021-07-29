import { UserInfos } from "src/app/model/user-infos";

export interface LoginResponse{
    refreshToken: string,
    userInfos: UserInfos
}