import { Button, ButtonProps } from "@chakra-ui/react"
import { FC } from "react"


/**
 * Большая зеленая кнопка в реестрах и не только
 * 
 * Используется там, где нужно максимально ярко выделить
 * возможность создания чего-либо 
 * @param props 
 * 
 * @returns 
 */
export const CreateButton: FC<ButtonProps> = (props) => {
    const ButtonStyleProps: ButtonProps = {
        colorScheme: 'teal',
        bg: "teal.400",
        h: "40px",
        marginLeft: "auto",
        minW: "200px",
        maxW: "200px",
        borderRadius: "10px",
        type: 'button',
        fontFamily: "Raleway-SemiBold",
        fontSize: "14px",
        position: "relative",
        bottom: "0px",
        left: "0px",
        ...props,
    }
    return (
        <Button {...ButtonStyleProps}>
            {props.children}
        </Button>
    )
}
