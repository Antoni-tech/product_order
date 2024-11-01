import { Privileges } from "./Privileges"

export type Role = {
    id?: number | string
    name: string
    privileges: Array<Privileges>
    default_role: boolean
}

export type RoleList = {
    roles: Array<Role>
}

