import {PayloadAction} from "@reduxjs/toolkit"
import {Dispatch} from "react"
import {ActionTypes} from "../../redux/Users/UserReducer"
import {UserRepositoryInstance} from "../../repository/User/UserRepository"
import {User, UserCreationRequest} from "../../shared/entities/Users/User"


/**
 * UserService.
 *
 */
export class UserService {
    private dispatch: Dispatch<PayloadAction<any, string>>
    private repository = UserRepositoryInstance

    constructor(dispatch: Dispatch<PayloadAction<any, string>>) {
        this.dispatch = dispatch
    }

    public async getUserList() {
        return this.repository.getUserList().then(res => {
            this.dispatch({
                type: ActionTypes.USER_LIST,
                payload: res?.Some || []
            })
            return res
        })
    }

    public async getUserListAll(params: { roleIds?: Array<number | string>; companyName?: string; page?: number; size?: number; login?: string; phone?: string, email?: string }) {
        if (params?.roleIds) {
            //@ts-ignore
            params.roleIds = params.roleIds?.join(",")
        }
        //@ts-ignore
        return await this.repository.getUserListAll(params).then(res => {
            this.dispatch({
                type: ActionTypes.USER_LIST_ALL,
                payload: res.Some
            })
            return res
        })

    }


    public async createUser(data: any) {
        return this.repository.createUser(data).then(res => {
            this.dispatch({
                type: ActionTypes.USER_CREATE,
                payload: res?.Some || data
            })
            return res
        })

    }

    public async changePassword(data: any) {
        return this.repository.changePassword(data).then(res => {
            this.dispatch({
                type: ActionTypes.CHANGE_PASSWORD,
                payload: res?.Some || data
            })
            return res
        })

    }

    public async restorePassword(data: any) {
        return this.repository.restorePassword(data).then(res => {
            return res
        })

    }

    public async getUser(data: any) {
        return await this.repository.getUser(data).then(res => {
            this.dispatch({
                type: ActionTypes.USER_GET,
                payload: res?.Some
            })
            return res
        })
    }

    public async updateUser(data: any) {
        return this.repository.updateUser(data).then(res => {
            this.dispatch({
                type: ActionTypes.USER_UPDATE,
                payload: res?.Some || {},
            })
            return res
        })
    }

    public cleanUser() {
        this.dispatch({
            type: ActionTypes.USER_GET,
            payload: {}
        })
    }

    public async deleteUser(id: number) {
        return await this.repository.deleteUser(id)
    }

    public async userCreationRequest(data: UserCreationRequest) {
        return await this.repository.userCreationRequest(data).then(res => {
            return res
        })
    }
}



