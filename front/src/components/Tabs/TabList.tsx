import { ChakraProps, TabList, TabListProps } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Компонент обертка для множества табов
 * @param props 
 * @returns 
 */
export const TabListStyled: FC<ChakraProps> = (props) => {
    const TabListStyledProps: TabListProps = {
        borderBottom: "3px solid",
        w: "fit-content",
        fontSize: "16px",
        ...props
    }

    return (
        <TabList {...TabListStyledProps} />
    )
}
