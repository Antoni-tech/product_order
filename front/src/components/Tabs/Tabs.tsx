import {ChakraProps, Tabs, TabsProps} from "@chakra-ui/react";
import {FC} from "react";

export const TabsStyled:FC<ChakraProps | TabsProps> = (props, children) => {
    const TabsStyledProps:TabsProps = {
        display: "flex",
        flexDir: "column",
        justifyContent: "space-between",
        colorScheme: "teal",
        variant: "line",
        color: "gray.400",
        fontSize: "18px",
        fontFamily: "Raleway-Bold",
        h: "100%",
        ...props,
        ...children
    }
    return (
        <Tabs {...TabsStyledProps}/>
    ) 
}

