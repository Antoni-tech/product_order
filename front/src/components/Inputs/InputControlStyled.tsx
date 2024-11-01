import { InputControl, InputControlProps } from "formik-chakra-ui";
import { FC } from "react";

/**
 * Компонент обертка для инпутов, стилизованный по умолчанию
 * @param props 
 * @returns 
 */
export const InputControlStyled: FC<InputControlProps> = (props) => {

    const InputControlStyleProps: InputControlProps = {
        ...props,
        inputProps: {
            minW: "200px",
            focusBorderColor: "blue.800",
            errorBorderColor: "red.400",
            marginTop: "5px",
            fontFamily: "Dosis",
            borderRadius: "4px",
            maxW: "250px",
            ...props.inputProps,
        },
        errorMessageProps: {
            color: "red.400",
            fontSize: "14px",
            display: "block",
            fontFamily: "Dosis",
            fontStyle: "italic",
            maxW: props?.inputProps?.maxW || "auto",
            ...props.errorMessageProps,
        },
    }


    return (
        <>
            <InputControl {...InputControlStyleProps} />
        </>
    )
}

