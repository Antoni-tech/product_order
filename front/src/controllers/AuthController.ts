import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { AuthorizationService } from "../service/Authorization/AuthorizationService";
import { AuthorizationData } from "../shared/entities/Auth/AuthorizationData";
import { Privileges } from "../shared/entities/Role/Privileges";
import { RoleList } from "../shared/entities/Role/Role";
import { Option } from "../shared/utilities/OptionT";

interface AuthorizationOperator {
    checkIfAuthorized(): Promise<Option<Array<Privileges>>>
    authorize(data: AuthorizationData): Promise<Option<RoleList>>
}

export class AuthorizationController implements AuthorizationOperator {
    private authorizationService: AuthorizationService
    private dispatch: Dispatch<PayloadAction<any>>

    constructor(dispatch: Dispatch<PayloadAction<any>>) {
        this.dispatch = dispatch
        this.authorizationService = new AuthorizationService(this.dispatch)
    }

    public async authorize(data: AuthorizationData): Promise<Option<RoleList>> {
        return this.authorizationService.authorize(data).then(res => res)
    }
    public async checkIfAuthorized(): Promise<Option<Array<Privileges>>> {
        return await this.authorizationService.checkAuth().then(res => res)
    }

    public async refresh(): Promise<boolean> {
        return await this.authorizationService.refresh().then(res => {
            if (res?.None || !res.Some) {
                return false;
            }
            return res.Some
        })
    }

    public async logOut(): Promise<boolean> {
        return await this.authorizationService.logOut().then(res => {
            if (res?.None || !res.Some) {
                return false;
            }
            return res.Some
        })

    }

    private ClearAuthorizationData(): void {
        sessionStorage.removeItem("auth")
    }
}

