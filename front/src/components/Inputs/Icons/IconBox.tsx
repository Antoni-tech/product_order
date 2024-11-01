import React, { FC } from "react";
import { ChakraProps, Flex } from "@chakra-ui/react";

/**
 * Контейнер для иконок
 * @param props 
 * @returns 
 */

type ContainerProps = {
  children: React.ReactNode;
};
export const IconBox = (props: ContainerProps) => {
  const { children, ...rest } = props;

  return (
    <Flex
      alignItems={"center"}
      justifyContent={"center"}
      borderRadius={"8px"}
      bgColor={"gray.800"}
      p={"8px"}
      {...rest}
    >
      {children}
    </Flex>
  );
}
