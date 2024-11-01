import {ResponseMessage, User, UserCreationRequest} from "../../shared/entities/Users/User";
import {CustomError} from "../../shared/errors/CustomErrors";
import {Option} from "../../shared/utilities/OptionT";
import {APIDao, Requester} from "../APIRequester";

/**
 * UserRepository.
 *
 * Responsible for fetching data from backend
 * by callind APIDao and handling various data
 * coming from API
 *
 * Data is passed to UserService in a form of
 * an @type Option<T> type, where @param{Some: T} is either
 * a valid data, expected from the server,
 * or, in case of error, a default safe value,
 * for an object of array (kinda like default
 * values for types in Go),and @param{None: CustomError}
 * is either an error or undefined
 */
export class UserRepository<T> {
    private dao: Requester

    constructor(dao: Requester) {
        this.dao = dao
    }

    /**
     * getUserList.
     *
     * @returns {Promise<Option<Array<User>>>}
     *
     */
    public async getUserList(): Promise<Option<Array<User>>> {
        return await this.dao.getRequest<{ USERS: Array<User> }>("admin-service/api/users/list", {}).then(res => {
            const result: Option<Array<User>> = {
                Some: res.data?.USERS,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<Array<User>> = {
                Some: [],
                None: err
            }
            return result
        })
    }

    public async getUserListAll(params: {}): Promise<Option<{ users: Array<User>, count: number }>> {
        return await this.dao.getRequest<{ users: Array<User>, count: number }>("admin-service/api/users/users_list_all", {params: {...params}}).then(res => {
            const result: Option<{ users: Array<User>, count: number }> = {
                Some: {
                    users: res?.data?.users,
                    count: res?.data?.count
                }
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<{ users: Array<User>, count: number }> = {
                //@ts-ignore
                Some: {
                    users: [],
                    count: 0
                },
                None: err
            }
            return result
        })
    }

    /**
     * getUser.
     *
     * @returns {Promise<Option<User>>}
     */
    public async getUser(params: {}): Promise<Option<User>> {
        return await this.dao.getRequest<User>("admin-service/api/users/login", {params: {...params}}).then(res => {
            const result: Option<User> = {
                Some: res?.data,
            }

            return result
        }).catch((err: CustomError) => {
            const result: Option<User> = {
                Some: {} as User,
                None: err
            }
            return result
        })
    }


    /**
     * createUser.
     *
     * @param {any} data
     * @returns {Promise<Option<number>>}
     */
    public async createUser(data: any): Promise<Option<number>> {
        return await this.dao.postRequest<number>("admin-service/api/admin/create_user", {data}).then(res => {
            const result: Option<number> = {
                Some: res?.data,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<number> = {
                Some: 0,
                None: err
            }
            return result
        })
    }

    public async restorePassword(data: any): Promise<Option<number>> {
        return await this.dao.postRequest<number>("admin-service/api/admin/restore-password", {data}).then(res => {
            const result: Option<number> = {
                Some: res?.data,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<number> = {
                Some: 0,
                None: err
            }
            return result
        })
    }

    public async changePassword(data: any): Promise<Option<number>> {
        return await this.dao.postRequest<number>("admin-service/api/admin/user_change_password", {data}).then(res => {
            const result: Option<number> = {
                Some: res?.data,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<number> = {
                Some: 0,
                None: err
            }
            return result
        })
    }
    /**
     * updateUser
     *
     * @param {any} data
     * @returns {Promise<Option<number>>}
     */
    public async updateUser(data: any): Promise<Option<number>> {
        return await this.dao.postRequest<number>("admin-service/api/admin/change_user", {data}).then(res => {
            const result: Option<number> = {
                Some: res?.data,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<number> = {
                Some: 0,
                None: err
            }
            return result
        })
    }

    public async deleteUser(id: number) {
        await this.dao.putRequest<User>(`admin-service/api/admin/delete/${id}`, {});
    }

    public async userCreationRequest(data: UserCreationRequest): Promise<Option<any>> {
        return await this.dao.postRequest<any>("admin-service/api/admin/request_user_creation", { data }).then(res => {
            const result: Option<any> = {
                Some: res.data,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<any> = {
                Some: {} as -1,
                None: err
            }
            return result
        })
    }
}

export const UserRepositoryInstance = new UserRepository(APIDao) 
