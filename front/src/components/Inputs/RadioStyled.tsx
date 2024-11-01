import { Radio, RadioGroup, RadioGroupProps, RadioProps } from "@chakra-ui/react";
import { RadioGroupControl, RadioGroupControlProps } from "formik-chakra-ui";
import { FC } from "react";

export const RadioStyled: FC<RadioProps> = (props) => {

    return <Radio colorScheme="teal" marginInlineStart="0px !important" {...props} />
}


/**
 * Радиокнопка, стилизованная по умолчанию
 * @param props 
 * @returns 
 */
export const RadioGroupStyled: FC<RadioGroupProps> = (props) => {
    const RadioGroupStyledProps: RadioGroupProps = {
        ...props,
        colorScheme: "teal",
        pl: "0px",
        ml: "0px",
        flexDir: "column",
        display: "flex",
        justifyContent: "flex-start",
        alignItems: "flex-start",
        maxW: "250px"
    }
    return <RadioGroup {...RadioGroupStyledProps}>
        {props.children}
    </RadioGroup>

}

/**
 * Группа радиокнопок, стилизованных по умолчанию
 * @param props 
 * @returns 
 */
export const RadioGroupControlStyled: FC<RadioGroupControlProps> = (props) => {
    const RadioGroupStyledProps: RadioGroupControlProps = {
        ...props,
        colorScheme: "teal",
        stackProps: {
            pl: "0px",
            ml: "0px",
            flexDir: "column",
            justifyContent: "flex-start",
            alignItems: "flex-start",
            maxW: "250px"
        }
    }
    return <RadioGroupControl {...RadioGroupStyledProps}>
        {props.children}
    </RadioGroupControl>
}
