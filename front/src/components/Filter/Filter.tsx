import { Box, Text, ChakraProps, Input, InputGroup, InputRightElement, Select, Checkbox } from "@chakra-ui/react"
import { FC, useState } from "react"
import { FaSearch, FaWindowClose } from "react-icons/fa"

/**
 * Filter.
 *
 * Represents default text input with
 * filter capabilities
 *
 * @param {specialLabel: string, updateFunc: Function}
 *
 */
export const Filter: FC<{ specialLabel?: string, uniqueName: string, updateFunc: Function, type?: "input" | "select" | "checkbox", options?: any, style?: ChakraProps
}> = ({ specialLabel, uniqueName, updateFunc, type, options, style }) => {
    const [val, setVal] = useState<string>("")
    const FilterDefaultStyleConfig: ChakraProps = {
        h: "30px",
        w: "200px",

        fontSize: "14px",
        color: "gray.700",
        paddingRight: "60px",
        _active: {
            borderColor: "gray.500"
        },
        _focus: {
            border: "2px solid",
            borderColor: "gray.500"
        },
        ...style
    }

    const update = (value: string) => {
        const x = {}
        //@ts-ignore
        x[uniqueName] = value
        setVal(value)
        updateFunc(x)
    }
    const handleKeyPress = (event: any) => {
        if (event.key === 'Enter') {
            setVal(event.target.value)
            update(event.target.value)
        }
    }

    return (
        <Box maxW="200px">
            <Text fontSize="14px">{specialLabel}</Text>

            {type === "select"
                ? <Select {...FilterDefaultStyleConfig} pr="0px" value={val} onChange={(ev: any) => update(ev.target.value)}>
                    {options}
                </Select>
                : type === "checkbox"
                    //@ts-ignore
                    ? <Box colorScheme="teal" styleConfig={{ flexDir: "row", display: "flex" }} onChange={(ev) => updateFunc(ev.target.value)}>
                        {options?.map((elem: any) => <Checkbox colorScheme={"teal"} key={elem.id} value={elem.id} children={elem.name} mr="10px" />)}
                    </Box>
                    :
                    <InputGroup>
                        <Input
                            {...FilterDefaultStyleConfig}
                            value={val}
                            onChange={(ev: any) => setVal(ev.target.value)}
                            onKeyPress={handleKeyPress}
                        />
                        <InputRightElement h="30px" cursor="pointer">
                            <FaSearch onClick={() => update(val)} />
                        </InputRightElement>
                        {val.length > 0 && <InputRightElement right="25px" h="30px" cursor="pointer">
                            <FaWindowClose onClick={() => update("")} />
                        </InputRightElement>}
                    </InputGroup>
            }
        </Box>
    )
}

