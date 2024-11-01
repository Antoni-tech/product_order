import { Box, ChakraProps, Spacer } from "@chakra-ui/react";
import { FC, ReactNode } from "react";
import { FaPen, FaRedo, FaRemoveFormat, FaTimes, FaTimesCircle, FaTrash } from "react-icons/fa";

type RegistryListRowFunctionalButtonConfig = {
    icon?: ReactNode
    text: string | number
    type?: "edit" | "delete" | "refresh" | "disconnect"
}

/**
 * Данный компонент содержит кнопки удаления или обновления
 * которые могут использоватсья внутри списочных элементов 
 * и обозначают удаление или обновление элемента списка
 * @param param0 
 * @returns 
 */
export const ProductListRowFunctionalButton: FC<{ cfg: RegistryListRowFunctionalButtonConfig }> = ({ cfg }) => {
    let DefaultIcon = undefined
    switch (cfg?.type) {
        case "delete":
            DefaultIcon = <FaTrash />
            break
        case "refresh":
            DefaultIcon = <FaRedo />
            break
        case "edit":
            DefaultIcon = <FaPen />
            break
        case "disconnect":
            DefaultIcon = <FaTimesCircle />
    }

    const DefaultStyleConfig: ChakraProps = {
        color: cfg?.type === "delete" || cfg?.type === "disconnect" ? "red.400" : "gray.500",
        display: "flex",
        flexDir: "row",
        justifyContent: "center",
        alignItems: "center",
        borderRadius: "5px",
        padding: "2px 10px",
        transition: ".2s all",
        _hover: {
            bgColor: cfg?.type === "delete" || cfg?.type === "disconnect" ? "red.100" : "gray.100",
            cursor: "pointer"
        },
    }
    return (
        <Box {...DefaultStyleConfig}>
            {DefaultIcon || cfg.icon}
            <Spacer w="5px" />
            {cfg.text}
        </Box>
    )
}
