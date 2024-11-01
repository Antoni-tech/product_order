import { useState, createContext, FC, useContext } from 'react';

class AuthErrorHandler {
    private handleFunc: Function = () => { }
    public handleAuthChange(arg: boolean | null) {
        this.handleFunc(arg)
    }
    public setHandleFunc(func: Function) {
        this.handleFunc = func
    }
}

const AuthorizationContext = createContext<any>(null)

export const authErrorHandler: AuthErrorHandler = new AuthErrorHandler()

/**
 * Хук авторизации
 * @param param0 
 * @returns 
 */
export const AuthProvider: FC<any> = ({ isAuthStatus, children }) => {
    const [isAuth, setIsAuth] = useState(isAuthStatus)
    const handleAuthChange = (status: boolean) => {
        setIsAuth(status)
    }
    authErrorHandler.setHandleFunc(handleAuthChange)
    return (
        <AuthorizationContext.Provider value={{ isAuth, handleAuthChange }}>{children}</AuthorizationContext.Provider>
    )
}

export const useAuth = () => useContext(AuthorizationContext)
