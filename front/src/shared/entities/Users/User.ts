import {Role} from "../Role/Role"

export type User = {
    id?: string
    login: string
    company?: string
    phone: string
    email?: string
    default_user: boolean
    roles: Array<Role>
    lastLogin: number;
    billingAddress?: Address
    shippingAddress?: Address
}

export type Address = {
    id: number
    state: State
    city: string
    street: string
    zip: string
    createdAt: number
}

export type State = {
    id: number
    code: string
    name: string
    tax: number
}

export type ResponseMessage = {
    status: string
}

export type UserCreationRequest = {
    name: string;
    dealership: string;
    email: string;
    phone: string;
}

export type ChangePasswordRequest = {
    login: string;
    code: string;
    oldPassword: string;
    newPassword: string;
}
export type RestoreRequest = {
    email: string;
}