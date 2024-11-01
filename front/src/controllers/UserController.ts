import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { UserService } from "../service/User/UserService";
import { Option } from "../shared/utilities/OptionT";
import {UserCreationRequest} from "../shared/entities/Users/User";

export class UserController {
    private dispatch: Dispatch<PayloadAction<any, string>>
    private service: UserService

    constructor(dispatch: Dispatch<PayloadAction<any, string>>) {
        this.dispatch = dispatch
        this.service = new UserService(this.dispatch)
    }


    public async getUserList() {
        return this.service.getUserList().then(res => {
            return this.handleResult(res)
        })
    }

    public async getUserListAll(params: { roleIds?: Array<number | string>; companyName?: string; page?: number; size?: number; login?: string; phone?: string, email?: string }) {
        return this.service.getUserListAll(params).then(res => {
            return res
        })
    }

    public async createUser(data: any): Promise<Option<boolean>> {
        return this.service.createUser(data).then(res => {
            return this.handleResult<number>(res)
        })
    }

    public async changePassword(data: any): Promise<Option<boolean>> {
        return this.service.changePassword(data).then(res => {
            return this.handleResult<number>(res)
        })
    }
    public async restorePassword(data: any): Promise<Option<boolean>> {
        return this.service.restorePassword(data).then(res => {
            return this.handleResult<number>(res)
        })
    }

    public async getUser(data: any) {
        return this.service.getUser(data)
    }

    public async updateUser(data: any): Promise<Option<boolean>> {
        return this.service.updateUser(data).then(res => {
            return this.handleResult(res)
        })
    }

    public cleanUser() {
        this.service.cleanUser()
    }

    public async deleteUser(id: number) {
        return this.service.deleteUser(id)
    }

    public async userCreationRequest(data: UserCreationRequest) {
        return this.service.userCreationRequest(data).then(res => res)
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
