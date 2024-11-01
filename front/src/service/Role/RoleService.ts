import { PayloadAction } from "@reduxjs/toolkit"
import { Dispatch } from "react"
import { RoleRepositoryInstance } from "../../repository/Role/RoleRepository"
import { ActionTypes } from "../../redux/Role/RoleReducer"
import { Role } from "../../shared/entities/Role/Role"

/**
 * RoleService.
 *
 */
export class RoleService {
    private dispatch: Dispatch<PayloadAction<any, string>>
    private repository = RoleRepositoryInstance

    constructor(dispatch: Dispatch<PayloadAction<any, string>>) {
        this.dispatch = dispatch
    }
    public async createRole(data: Role) {
        return this.repository.createRole(data).then(res => {
            return res
        })
    }
    public async getRole(id?: Array<number>) {
        if (id) {
            //@ts-ignore
            id = id?.join(",")
        }
        //@ts-ignore
        return this.repository.getRole(id).then(res => {
            this.dispatch({
                type: ActionTypes.ROLE_GET,
                payload: res?.Some
            })
            return res
        })
    }
    public async getPrivileges(id?: Array<number>) {
        if (id?.length !== 0) {
            //@ts-ignore
            id = id?.join(",")
        }
        //@ts-ignore
        return this.repository.getPrivileges(id).then(res => {
            this.dispatch({
                type: ActionTypes.PRIVILEGES_GET,
                payload: res?.Some
            })
            return res
        })
    }

    public async updateRole(data: Role) {
        return this.repository.updateRole(data).then(res => {
            this.dispatch({
                type: ActionTypes.ROLE_UPDATE,
                payload: res?.Some || {},
            })
            return res
        })
    }

    public cleanRole() {
        this.dispatch({
            type: ActionTypes.ROLE_GET,
            payload: []
        })
    }

    public async deleteRole(id: number) {
        return await this.repository.deleteRole(id)
    }
}