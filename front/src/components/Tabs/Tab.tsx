import { Tab, TabProps } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Табы
 * Логически разделяют страницы в секциях системы 
 * @param props 
 * @returns 
 */
export const TabStyled: FC<TabProps> = (props) => {
    const TabStyledConfig: TabProps = {
        minW: "110px",
        mb: "-3px",
        borderBottom: "3px solid",
        _selected: {
            color: "teal.400",
            borderColor: "currentColor",
        },
        boxShadow: "none !important",
        bgColor: "transparent !important",
        ...props
    }
    return (
        <Tab {...TabStyledConfig} />
    )
}
