import { SelectControl, SelectControlProps } from "formik-chakra-ui";
import { FC } from "react";

/**
 * Селект, стилизованный по умолчанию
 * @param props 
 * @returns 
 */
export const SelectControlStyled: FC<SelectControlProps> = (props) => {
    const SelectControlStyledProps: SelectControlProps = {
        selectProps: {
            focusBorderColor: "blue.800",
            errorBorderColor: "red.400",
            marginTop: "5px",
            cursor: "pointer",
            fontFamily: "Raleway-Regular",
            borderRadius: "4px",
            ...props.selectProps
        },
        errorMessageProps: {
            color: "red.400",
            fontSize: "14px",
            display: "block",
            fontFamily: "Raleway-Regular",
            fontStyle: "italic",
            ...props.errorMessageProps,
        },
        ...props
    }

    return (
        <SelectControl {...SelectControlStyledProps} />
    )
}

