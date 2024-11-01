import { Button, ChakraProps, Flex } from "@chakra-ui/react";
import { FC } from "react";

type FormButtonsRowConfig = {
    submitButton?: {
        disabled: boolean,
        text: string,
        clickAction?: Function,
        isSubmitting?: boolean,
        type?: "submit" | "button"
    },
    cancelButton?: {
        text: string,
        cancelFunc: Function
    }
}

/**
 * Компонент с набором кнопок, часто используемых в формочках
 * 
 * @param param0 
 * @returns 
 */
export const FormButtonsRow: FC<{ config: FormButtonsRowConfig }> = ({ config }) => {
    const FormButtonsRowStyleConfig: ChakraProps = {
        w: "100%",
        flexDir: "row",
        justifyContent: "flex-end",
        alignItems: "center",
        alignSelf: "flex-end",
        position: "absolute",
        bottom: "0px"
    }

    const ButtonsGeneralStyleConfig: ChakraProps = {
        border: "solid 1px",
        fontSize : "small",
        borderRadius:"8px",
        height:"35px",
        px: "25px",
        color:"#5A5A5A",
        background:"#EFEFEF",
        _hover: {
            bgColor: "gray.200",
        },
        _active: {
            bgColor: "gray.400",
        },
    }
    const CancelButtonStyleConfig: ChakraProps = {
        mr: "7px",
        ...ButtonsGeneralStyleConfig
    }

    const SubmitButtonStyleConfig: ChakraProps = {
        ml: "7px",
        ...ButtonsGeneralStyleConfig
    }
    return (
        <Flex {...FormButtonsRowStyleConfig}>
            {config.cancelButton && <Button
                {...CancelButtonStyleConfig}
                onClick={() => config.cancelButton?.cancelFunc()}>
                {config.cancelButton.text}
            </Button>}
            {config.submitButton && <Button
                {...SubmitButtonStyleConfig}
                type={config.submitButton?.type ? config.submitButton?.type : "button"}
                onClick={() => config.submitButton?.clickAction && config.submitButton?.clickAction()}
                isLoading={config?.submitButton?.isSubmitting || false}
                disabled={config?.submitButton?.isSubmitting ? config?.submitButton?.isSubmitting : config?.submitButton?.disabled}>
                {config?.submitButton?.text || ""}
            </Button>}
        </Flex>
    )
}
