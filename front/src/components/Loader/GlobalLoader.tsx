import { Flex, Spinner } from "@chakra-ui/react";
import { FC } from "react";

/**
 * Элемент, отображающий анимацию загрузки
 * @returns 
 */
export const GlobalLoader: FC = () => {
    return (
        <>
            <Flex w="100%" h="100%" >
                <Spinner
                    color="gray.400"
                    width="250px"
                    height="250px"
                    alignSelf="center"
                    m="auto">
                </Spinner>
            </Flex>
        </>
    )
}