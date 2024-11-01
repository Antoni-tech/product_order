export type APIError = {
    code: string | number
    message: string
} | undefined 

export interface CustomError {
    getErrorCode():string | number 
    getErrorDescription(): string
}

export class ErrorGenerator extends Error implements CustomError {
    private errorData:APIError          

    constructor(errorData: APIError){
        super(errorData?.message)
        this.errorData = errorData
    }

    getErrorCode(): string | number {
        return this.errorData?.code || 0
    }

    getErrorDescription(): string {
       return this.errorData?.message|| ""
    }
}
