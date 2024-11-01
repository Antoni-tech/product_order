import { Box, ChakraProps, Flex, Link } from "@chakra-ui/react";
import { FC, MouseEventHandler, ReactNode } from "react";
import { CSSObject } from "@emotion/react";

/**
 * ListItemConfig.
 */
export type ListItemConfig = {
	content: string | number | ReactNode
	isLink: boolean,
	clickAction?: Function,
	style?: ChakraProps
}

/**
 * Строка в списочных компонентах, используется, например,
 *  в реестрах
 * @param param0 
 * @returns 
 */
export const ListItemRow: FC<{ list: Array<ListItemConfig>, noBorder?: boolean, style?: ChakraProps, clickAction?: MouseEventHandler<HTMLDivElement> }> = ({ list, noBorder, style, clickAction }) => {
	const DefaultListItemConfig: ChakraProps = {
		borderBottom: noBorder ? "none" : "1px solid #71809640",
		h: "auto",
		w: "100%",
		justifyContent: "space-between",
		...style
	}
	const List: Array<ReactNode> = list.map((config: ListItemConfig, idx: number) => {
		return <ListItem key={idx} {...config} />
	})


	const BoxConfig: ChakraProps = {
		width: "100%",
		padding: "10px 0 10 0",
		// background: "#E2E8F0" ,
		transition: "background 0.3s",
	}

	return (
		<Box {...BoxConfig}>
			<Flex {...DefaultListItemConfig} onClick={clickAction ? clickAction : () => { }}>
				{List}
			</Flex>
		</Box>

	)
}

export const ListLinkItemDefaultStyleConfig: CSSObject = {
	color: "gray.800",
	textDecoration: "none",
	_hover: {
		color: "gray.800",
		textDecoration: "underline",
		borderRadius: "5px",
	},
};



/**
 * Ячейка в строке списка
 * @param param0 
 * @returns 
 */
const ListItem: FC<ListItemConfig> = ({ isLink, content, clickAction, style }) => {
	const ListItemDefaultStyleCOnfig: ChakraProps = {
		display: "flex",
		justifyContent: "center",
		alignItems: "center",
		width: "fit-content",
		textAlign: "center",
		fontSize: "18px",
		fontFamily: "Dosis",
		color: "gray.800",
		textOverflow: "ellipsis",
		p: "5px 0px",
		...style
	}


	return (
		<Box {...ListItemDefaultStyleCOnfig} onClick={() => clickAction && clickAction()}>
			{/* {isLink ? <Link sx={ListLinkItemDefaultStyleConfig}>{content}</Link> : content} */}
		</Box>
	)
}
