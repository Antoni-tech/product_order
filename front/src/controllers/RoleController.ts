import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { RoleService } from "../service/Role/RoleService";
import { Role} from "../shared/entities/Role/Role";
import { Option } from "../shared/utilities/OptionT";

export class RoleController {
    private dispatch: Dispatch<PayloadAction<any>>
    private role: RoleService
    private privileges: RoleService
    constructor(dispatch: Dispatch<PayloadAction<any>>) {
        this.dispatch = dispatch
        this.role = new RoleService(this.dispatch)
        this.privileges = new RoleService(this.dispatch)
    }
    public async getRole(id?: Array<number>): Promise<Option<Array<Role>>> {
        return this.role.getRole(id).then(res => res)
    }
    public async getPrivileges(id?: Array<number>) {
        return this.privileges.getPrivileges(id).then(res => res)
    }
    public async createRole(data: Role): Promise<Option<boolean>> {
        return this.role.createRole(data).then(res => {
            return this.handleResult<number>(res)
        })
    }
    public async updateRole(data: Role): Promise<Option<boolean>> {
        return this.role.updateRole(data).then(res => {
            return this.handleResult(res)
        })
    }

    public async deleteRole(id: number) {
        return this.role.deleteRole(id)
    }

    public cleanRole() {
        this.role.cleanRole()
    }

    private handleResult<T>(res: Option<T>): Option<boolean> {
        if (res?.None) {
            return {
                Some: false,
                None: res.None
            }
        }
        return { Some: true }
    }
}