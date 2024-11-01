import { Button, ChakraProps, Flex } from "@chakra-ui/react";
import { FC, ReactNode } from "react";
import {AngleDoubleLeft, AngleDoubleRight, AngleLeft, AngleRight} from "../Inputs/Icons/Icons";
export type SwapPageTrigger = (position: number) => void

/**
 * Пагинатор
 * @param param0
 * @returns
 */
export const Paginator: FC<{ limit: number, current: number, swapPageTrigger: SwapPageTrigger }> = ({ limit, current, swapPageTrigger }) => {
    const PaginatorButtonStyleConfig: ChakraProps = {
        bg: "#fff",
        color: "#5A5A5A",
        padding: "0px",
        fontFamily:"Oswald",
        fontWeight:"400",
        fontSize:"18px",
        _hover: {
            color: "gray.800",
        },
        _active: {
            bg: "#fff",
            color: "gray.800",
        }
    }

    const PaginatorStyleConfig: ChakraProps = {
        flexDir: "row",
        justifyContent: "flex-start",
        alignItems: "center",
        mt: "auto",
        mb: "0px"
    }
    const numericButtonsAmount: number = Math.round(limit / 10 >= 5 ? 5 : limit / 10)

    /**
     * Функция расчета номеров в кнопках пагинатора
     */
    const buttonsList: ReactNode = new Array(numericButtonsAmount).fill(numericButtonsAmount).map((_, idx) => {
        return <Button key={idx + 1} {...PaginatorButtonStyleConfig}
            isActive={current <= 3 ? current === idx + 1 : idx === 2}
            onClick={() => (current - 3 + idx) * 10 >= limit ? undefined : swapPageTrigger(current <= 3 ? idx + 1 : current - 2 + idx)}>
            {current <= 3 ? idx + 1 : current - 2 + idx}
        </Button>
    })

    return (
        <Flex {...PaginatorStyleConfig}>
            <Button {...PaginatorButtonStyleConfig} children={<AngleLeft fontSize={"24px"}/>} onClick={() => swapPageTrigger(1)} />
            <Button {...PaginatorButtonStyleConfig} children={<AngleDoubleLeft fontSize={"24px"}/>} onClick={() => current > 1 ? swapPageTrigger(current - 1) : undefined} />
            {buttonsList}
            <Button {...PaginatorButtonStyleConfig} children={<AngleDoubleRight fontSize={"24px"}/>} onClick={() => current < Math.ceil(limit / 10) ? swapPageTrigger(current + 1) : undefined} />
            <Button {...PaginatorButtonStyleConfig} children={<AngleRight fontSize={"24px"}/>} onClick={() => swapPageTrigger(Math.ceil(limit / 10))} />
        </Flex>
    )
}
