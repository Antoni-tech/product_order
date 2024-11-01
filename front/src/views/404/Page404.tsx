import { Flex, Text } from "@chakra-ui/react";
import { FC } from "react";
import { SectionCard } from "../../components/Card/SectionCard";

export const Page404: FC = () => {

    return <SectionCard>
        <Flex flexDir={"column"}
            justifyContent={"center"} alignItems={"center"} height={"100%"}>
            <Text
                color={"gray.600"}
                fontSize={{ xl: "48px" }}
                fontFamily={"Raleway-Regular"}
            >
                404
            </Text>
            <Text
                color={"gray.600"}
                fontSize={{ xl: "48px" }}
                fontFamily={"Raleway-Regular"}
            >
                This page does not exist
            </Text>
        </Flex>
    </SectionCard>
}