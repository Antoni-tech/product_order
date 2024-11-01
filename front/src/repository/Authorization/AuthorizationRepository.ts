import { AuthorizationData } from "../../shared/entities/Auth/AuthorizationData";
import { Privileges } from "../../shared/entities/Role/Privileges";
import { RoleList } from "../../shared/entities/Role/Role";
import { CustomError } from "../../shared/errors/CustomErrors";
import { Option } from "../../shared/utilities/OptionT";
import { APIDao, Requester } from "../APIRequester";

class AuthorizationRepository<T> {
    private dao: Requester

    constructor(dao: Requester) {
        this.dao = dao
    }

    public async authorize(data: T): Promise<Option<RoleList>> {
        //@ts-ignore
        sessionStorage.setItem("login", data.login)
        return await this.dao.postRequest<RoleList>("auth-service/api/auth/user", { data }).then(res => {
            const result: Option<RoleList> = {
                Some: res?.data
            }
            return result
        }).catch((err: CustomError) => {
            sessionStorage.removeItem("login")
            const result: Option<RoleList> = {
                Some: { roles: [] },
                None: err
            }
            return result
        })
    }

    public async checkAuh(): Promise<Option<Array<Privileges>>> {
        return await this.dao.postRequest<{ login: String, privileges: Array<Privileges> }>("auth-service/api/auth/validate", {}).then(res => {
            sessionStorage.setItem("login", String(res?.data?.login))
            const result: Option<Array<Privileges>> = {
                Some: res?.data?.privileges
            }
            return result
        }).catch((err: CustomError) => {
            sessionStorage.removeItem("login")
            const result: Option<Array<Privileges>> = {
                Some: [],
                None: err
            }
            return result
        })
    }

    public async refresh(): Promise<Option<boolean>> {
        return await this.dao.postRequest("auth-service/api/auth/refresh", {}).then(res => {
            const result: Option<boolean> = {
                Some: true
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<boolean> = {
                Some: false,
                None: err
            }
            return result
        })
    }

    public async logOut(): Promise<Option<boolean>> {
        return await this.dao.postRequest("auth-service/api/auth/logout", {}).then(res => {
            const result: Option<boolean> = {
                Some: true
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<boolean> = {
                Some: false,
                None: err
            }
            return result
        })
    }
}

export const AuthorizationRepositoryInstance = new AuthorizationRepository<AuthorizationData>(APIDao)
