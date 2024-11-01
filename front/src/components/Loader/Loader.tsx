import { Spinner } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Анимированный элемент загрузки 
 * @returns 
 */
export const Loader: FC = () => {

    return (
        <Spinner
            color="gray.400"
            width="80px"
            height="80px"
            alignSelf="center"
            m="auto">
        </Spinner>
    )
}
