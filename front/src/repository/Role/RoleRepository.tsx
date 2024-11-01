import { CustomError } from "../../shared/errors/CustomErrors"
import { Option } from "../../shared/utilities/OptionT";
import { Role } from "../../shared/entities/Role/Role";
import { APIDao, Requester } from "../APIRequester";
import { Privileges } from "../../shared/entities/Role/Privileges";
import {User} from "../../shared/entities/Users/User";

/**
 * RoleRepository.
 * 
 * Responsible for fetching data from backend
 * by callind APIDao and handling various data
 * coming from API
 *
 * Data is passed to ServiceRepository in a form of 
 * an @type Option<T> type, where @param{Some: T} is either
 * a valid data, expected from the server,
 * or, in case of error, a default safe value,
 * for an object of array (kinda like default
 * values for types in Go),and @param{None: CustomError} 
 * is either an error or undefined 
 */


export class RoleRepository {
    private dao: Requester

    constructor(dao: Requester) {
        this.dao = dao
    }
    /**
     * createRole
     *
     * @param {any} data
     * @returns {Promise<Option<number>>}
     */
    public async createRole(data: Role): Promise<Option<number>> {
        return await this.dao.postRequest<number>("admin/create_role", { data }).then((res: { data: any; }) => {
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
     *  changeRole
     *
     * @param {any} data
     * @returns {Promise<Option<number>>}
     */
    public async updateRole(data: Role): Promise<Option<number>> {
        return await this.dao.postRequest<number>("admin/change_role", { data }).then((res: { data: any; }) => {
            const result: Option<number> = {
                Some: 1,
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
     *  getRole
     *
     * @param {any}id
     * @returns {Promise<Option<number>>}
     */
    public async getRole(id?: Array<number>): Promise<Option<Array<Role>>> {
        return await this.dao.getRequest<{ roles: Array<Role> }>("admin-service/api/roles", { params: { id } }).then((res: { data: { roles: Array<Role> } }) => {
            const result: Option<Array<Role>> = {
                Some: res?.data.roles,
            }
            return result
        }).catch((err: CustomError) => {
            const result: Option<Array<Role>> = {
                Some: [],
                None: err
            }
            return result
        })
    }

    /**
     *  getprivileges
     *
     * @param {any}id
     * @returns {Promise<Option<number>>}
     */

    public async getPrivileges(id?: string): Promise<Option<Array<Privileges>>> {
        return await this.dao.getRequest<{ PRIVILEGES: Array<Privileges> }>("admin/privileges", { params: { id } }).then((res: { data: { PRIVILEGES: Array<Privileges> } }) => {
            const result: Option<Array<Privileges>> = {
                Some: res?.data.PRIVILEGES
            }
            return result
        })
            .catch((err: CustomError) => {
                const result: Option<Array<Privileges>> = {
                    Some: [],
                    None: err
                }
                return result
            })
    }

    public async deleteRole(id: number) {
        await this.dao.putRequest<Role>(`admin/role/delete/${id}`, {});
    }

}



export const RoleRepositoryInstance = new RoleRepository(APIDao) 
