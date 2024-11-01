import { Button, ButtonProps } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Обычная функциональная кнопка, стилизованная по умолчнию
 * @param props 
 * @returns 
 */
export const ButtonStyled: FC<ButtonProps> = (props) => {
    const ButtonStyleProps: ButtonProps = {
        mt: 6,
        colorScheme: 'teal',
        bg: "teal.400",
        marginX: "auto",
        h: "52px",
        w: "100%",
        borderRadius: "10px",
        type: 'submit',
        ...props,
    }
    return (
        <Button {...ButtonStyleProps} />
    )
}
