import {CustomError} from "../errors/CustomErrors";

export type Option<T> = {
       Some: T
       None?: CustomError 
}

