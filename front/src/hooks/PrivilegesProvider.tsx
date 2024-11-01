import {createContext, FC, useContext, useState} from "react"
import {Privileges} from "../shared/entities/Role/Privileges"

export type PrivilegeMap = {
    USER_CREATE: boolean
    USER_EDIT: boolean
    USER_REMOVE: boolean
    USER_VIEW: boolean

    ROLE_CREATE: boolean
    ROLE_EDIT: boolean
    ROLE_REMOVE: boolean
    ROLE_VIEW: boolean

    ADMINISTRATOR_CREATE: boolean
    ADMINISTRATOR_EDIT: boolean
    ADMINISTRATOR_REMOVE: boolean
    ADMINISTRATOR_VIEW: boolean

    SETTING_CREATE: boolean
    SETTING_EDIT: boolean
    SETTING_REMOVE: boolean
    SETTING_VIEW: boolean

    PRODUCT_CREATE: boolean
    PRODUCT_EDIT: boolean
    PRODUCT_REMOVE: boolean
    PRODUCT_VIEW: boolean

    ORDER_CREATE: boolean
    ORDER_EDIT: boolean
    ORDER_REMOVE: boolean
    ORDER_VIEW: boolean
}

const PrivilegesContext = createContext<any>(null)

/**
 * Хук проверки привилеий
 * @param param0
 * @returns
 */
export const PrivilegesProvider: FC<any> = ({privileges, children}) => {
    const [priv, setPrivileges] = useState(privileges)

    return (
        <PrivilegesContext.Provider value={{priv, setPrivileges}}>{children}</PrivilegesContext.Provider>
    )
}

export const usePrivileges = () => useContext(PrivilegesContext)

export const defaultPrivilegeMap: PrivilegeMap = {
    USER_CREATE: false,
    USER_EDIT: false,
    USER_REMOVE: false,
    USER_VIEW: false,

    ROLE_CREATE: false,
    ROLE_EDIT: false,
    ROLE_REMOVE: false,
    ROLE_VIEW: false,

    ADMINISTRATOR_CREATE: false,
    ADMINISTRATOR_EDIT: false,
    ADMINISTRATOR_REMOVE: false,
    ADMINISTRATOR_VIEW: false,

    SETTING_CREATE: false,
    SETTING_EDIT: false,
    SETTING_REMOVE: false,
    SETTING_VIEW: false,

    PRODUCT_CREATE: false,
    PRODUCT_EDIT: false,
    PRODUCT_REMOVE: false,
    PRODUCT_VIEW: false,

    ORDER_CREATE: false,
    ORDER_EDIT: false,
    ORDER_REMOVE: false,
    ORDER_VIEW: false,
}

export const buildPrivilegesMap = (priv: Array<Privileges>): PrivilegeMap => {
    const privilegeMap: PrivilegeMap = {
        USER_CREATE: false,
        USER_EDIT: false,
        USER_REMOVE: false,
        USER_VIEW: false,

        ROLE_CREATE: false,
        ROLE_EDIT: false,
        ROLE_REMOVE: false,
        ROLE_VIEW: false,

        ADMINISTRATOR_CREATE: false,
        ADMINISTRATOR_EDIT: false,
        ADMINISTRATOR_REMOVE: false,
        ADMINISTRATOR_VIEW: false,

        SETTING_CREATE: false,
        SETTING_EDIT: false,
        SETTING_REMOVE: false,
        SETTING_VIEW: false,

        PRODUCT_CREATE: false,
        PRODUCT_EDIT: false,
        PRODUCT_REMOVE: false,
        PRODUCT_VIEW: false,

        ORDER_CREATE: false,
        ORDER_EDIT: false,
        ORDER_REMOVE: false,
        ORDER_VIEW: false,
    }
    const tagToFlagMapping: { [key: string]: keyof PrivilegeMap } = {
        'Users.create': 'USER_CREATE',
        'Users.edit': 'USER_EDIT',
        'Users.remove': 'USER_REMOVE',
        'Users.view': 'USER_VIEW',
        'Roles.create': 'ROLE_CREATE',
        'Roles.edit': 'ROLE_EDIT',
        'Roles.remove': 'ROLE_REMOVE',
        'Roles.view': 'ROLE_VIEW',
        'Administrators.create': 'ADMINISTRATOR_CREATE',
        'Administrators.edit': 'ADMINISTRATOR_EDIT',
        'Administrators.remove': 'ADMINISTRATOR_REMOVE',
        'Administrators.view': 'ADMINISTRATOR_VIEW',
        'Settings.create': 'SETTING_CREATE',
        'Settings.edit': 'SETTING_EDIT',
        'Settings.remove': 'SETTING_REMOVE',
        'Settings.view': 'SETTING_VIEW',
        'Products.create': 'PRODUCT_CREATE',
        'Products.edit': 'PRODUCT_EDIT',
        'Products.remove': 'PRODUCT_REMOVE',
        'Products.view': 'PRODUCT_VIEW',
        'Orders.create': 'ORDER_CREATE',
        'Orders.edit': 'ORDER_EDIT',
        'Orders.remove': 'ORDER_REMOVE',
        'Orders.view': 'ORDER_VIEW'
    };

    priv.forEach((elem: Privileges) => {
        const {privilegeGroup, tag} = elem;
        const mappingKey = `${privilegeGroup}.${tag}`;

        if (tagToFlagMapping[mappingKey]) {
            privilegeMap[tagToFlagMapping[mappingKey]] = true;
        }
    });
    return privilegeMap;
}
