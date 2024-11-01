import {Button, ButtonProps} from "@chakra-ui/react";
import {FC} from "react";

/**
 * Обычная функциональная кнопка, стилизованная по умолчнию
 * @param props
 * @returns
 */

export const FilterListButton: FC<ButtonProps> = (props) => {
    const ButtonStyleProps: ButtonProps = {
        variant: "outline",
        cursor: "pointer",
        borderRadius: "8px",
        border: "solid 0.8px",
        borderColor: "gray.600",
        height: "30px",
        fontSize: "15px",
        fontFamily: "Oswald",
        fontWeight: "300",
        ml: "10px",
        ...props,
    };
    return (
        <Button {...ButtonStyleProps} />
    )
}
