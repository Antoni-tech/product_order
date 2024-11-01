import {ChakraProps} from "@chakra-ui/react";
import {Card} from "./Card"
import React from "react";

type ContainerProps = {
    children: React.ReactNode;
};
export const SectionCard = (props: ContainerProps) => {
    const SectionCardStyledConfig: ChakraProps = {
        w: "100%",
        h: "85%",
        ...props,
    }
    return (
        <Card {...SectionCardStyledConfig}/>
    )
}
