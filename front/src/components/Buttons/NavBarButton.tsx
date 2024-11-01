import {Box, Button, ButtonProps} from "@chakra-ui/react";
import React, { FC } from "react";


/**
 * Обычная функциональная кнопка, стилизованная по умолчнию
 * @param props 
 * @returns 
 */

export const NavBarButton: FC<ButtonProps> = (props) => {
    const ButtonStyleProps: ButtonProps = {
        lineHeight:"35px",
        fontFamily:"Oswald",
        fontSize:"18px",
        fontWeight:"400",
        textDecoration: "none",
        display: "inline-block",
        cursor: "pointer",
        bg: "transparent",
        _hover: {
            color: "black.500",
            textDecoration: "underline",
            bg: "transparent",
        },
        ...props,
    };
    return (
        <Button {...ButtonStyleProps} >
            {/*/!* eslint-disable-next-line react/jsx-no-undef *!/*/}
            {/*<Box display="flex" alignItems="center">*/}
            {/*    /!* Add the PNG image as an avatar *!/*/}
            {/*    /!* eslint-disable-next-line react/jsx-no-undef *!/*/}
            {/*    <Image*/}
            {/*        src="/path/to/your/avatar.png"*/}
            {/*        alt="Avatar"*/}
            {/*        boxSize="30px" // Adjust the size as needed*/}
            {/*        borderRadius="full" // Makes it circular*/}
            {/*        marginRight="10px" // Adjust the spacing as needed*/}
            {/*    />*/}
            {/*    {props.children}*/}
            {/*</Box>*/}
        </Button>
    )
}
