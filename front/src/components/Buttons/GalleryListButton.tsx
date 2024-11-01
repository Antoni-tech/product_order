import {Button, ButtonProps} from "@chakra-ui/react";
import {FC} from "react";

/**
 * Обычная функциональная кнопка, стилизованная по умолчнию
 * @param props
 * @returns
 */

export const GalleryListButton: FC<ButtonProps> = (props) => {
    const ButtonStyleProps: ButtonProps = {
        variant: "outline",
        marginRight: "5px",
        cursor: "pointer",
        borderRadius: "10px",
        border: "solid 1px",
        borderColor: "gray.600",
        height: "28px",
        fontSize: "16px",
        fontFamily: "Dosis",
        fontWeight: "500",
        ml: "auto",
        ...props,
    };
    return (
        <Button {...ButtonStyleProps} />
    )
}
