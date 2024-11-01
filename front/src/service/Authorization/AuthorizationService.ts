import { PayloadAction } from "@reduxjs/toolkit";
import { Dispatch } from "react";
import { AuthorizationRepositoryInstance } from "../../repository/Authorization/AuthorizationRepository";
import { AuthorizationData } from "../../shared/entities/Auth/AuthorizationData";
import { Privileges } from "../../shared/entities/Role/Privileges";
import { RoleList } from "../../shared/entities/Role/Role";
import { Option } from "../../shared/utilities/OptionT";

export class AuthorizationService {
    private dispatch: Dispatch<PayloadAction<any, string>>
    private repository = AuthorizationRepositoryInstance

    constructor(dispatch: Dispatch<PayloadAction<any, string>>) {
        this.dispatch = dispatch
    }

    public async authorize(data: AuthorizationData): Promise<Option<RoleList>> {
        return this.repository.authorize(data).then(res => res)
    }

    public async checkAuth(): Promise<Option<Array<Privileges>>> {
        return this.repository.checkAuh().then(res => res)
    }

    public async refresh() {
        return this.repository.refresh().then(res => res)
    }

    public async logOut() {
        return this.repository.logOut().then(res => res)
    }
}


