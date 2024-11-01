import { ChakraProps } from "@chakra-ui/react";
import { MouseEventHandler, ReactNode } from "react";

export type CustomModalConfig = {
    style?: ChakraProps,
    isOpen: boolean,
    onOpen?: Function,
    onClose: () => void | MouseEventHandler<HTMLButtonElement>,
    onSubmit?: Function
    modalTextData?: string | ReactNode,
    idToDelete?: number | string
    singleButton?: boolean
    context: "error" | "action" | "action_one"
}


