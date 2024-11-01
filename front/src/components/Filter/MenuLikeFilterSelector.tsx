import { FC, useState } from "react";
import { Box, Button, ChakraProps, Checkbox, Menu, MenuButton, MenuItemOption, MenuList, MenuOptionGroup, Text } from "@chakra-ui/react"

/**
 * Компонент для фильтрации данных в реестрах, 
 * визуально похожий на выпадающее меню
 * @param param0 
 * @returns 
 */
export const MenuLikeFilterSelector: FC<{ specialLabel: string, uniqueName: string, updateFunc: Function, options?: any, style?: ChakraProps }> = ({ specialLabel, uniqueName, updateFunc, options, style }) => {
    const [val, setVal] = useState<Array<string | number> | undefined>([])
    const [isOpen, setIsOpen] = useState<boolean>(false)
    const FilterDefaultStyleConfig: ChakraProps = {
        h: "30px",
        fontSize: "14px",
        color: "gray.700",
        paddingRight: "60px",
        display: "flex",
        flexDir: "column",
        justifyContent: "flex-start",
        alignItems: "center",
        fontFamily: "Oswald",
        _active: {
            borderColor: "gray.500"
        },
        _focus: {
            border: "2px solid",
            borderColor: "gray.500"
        },
        ...style
    }
    return (
        <Box p="16px 10px 0px 0px" minW="200px">
            <Text fontSize="14px">{specialLabel}</Text>
            <Menu {...FilterDefaultStyleConfig} closeOnSelect={false} isOpen={isOpen}>

                <MenuButton as={Button} fontSize="14px" maxH="30px" borderRadius="5px" colorScheme='gray' onClick={() => setIsOpen(true)}>
                    Select statuses
                </MenuButton>
                <MenuList minW="140px">
                    <MenuOptionGroup type='checkbox' onChange={(ev) => {
                        //@ts-ignore
                        setVal(ev)
                    }}>
                        {options?.map((elem: any) => <MenuItemOption key={elem.id} value={elem.id} children={elem.name} mr="10px" />)}
                        <Box w="200px" flexDir="row"
                            justifyContent="flex-end" alignItems="flex-end">
                            <Button m="5px 60%" h="25px" borderRadius="5px" type="button" colorScheme="gray" onClick={() => {
                                updateFunc(val)
                                setIsOpen(false)
                            }}>
                                Accept
                            </Button>
                        </Box>
                    </MenuOptionGroup>
                </MenuList>
            </Menu>
        </Box>
    )
}
