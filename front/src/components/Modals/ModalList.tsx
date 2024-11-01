import React, { FC, useEffect, useState } from "react";
import {
	Box,
	Button,
	ChakraProps,
	Flex,
	Modal,
	ModalBody,
	ModalCloseButton,
	ModalContent,
	ModalFooter,
	ModalOverlay,
	Text,
} from "@chakra-ui/react";
import { ConnectorInputController } from "../../controllers/ConnectorInputController";
import { useAppSelector } from "../../redux/Store";
import { useDispatch } from "react-redux";
import { ListItemConfig, ListItemRow } from "../List/ListItem";
import { MainButton } from "../Buttons/MainButton";
import { ActionsType, ComponentType } from "../../common/constants";
import { Option } from "../../shared/utilities/OptionT";
import { ConnectorOutputController } from "../../controllers/ConnectorOutputController";
import { RulesController } from "../../controllers/RulesController";
import { ItemModel } from "../../shared/entities/Connector/Connector";
import { DHTMLXTypes, EventObject } from "../../redux/DHTMLX/DHTMLReducer";

interface ModelListProps {
	isOpen?: boolean;
	onClose?: () => void;
	state: ComponentType;
	style?: ChakraProps;
}

interface T {
}

export const ModalList: FC<ModelListProps> = ({
	style,
	isOpen,
	onClose,
	state,
}) => {
	const dispatch = useDispatch();
	const [close, setClose] = useState<boolean>(true);
	const [list, setList] = useState<JSX.Element[]>([]);
	const [lastEvent, setLastEvent] = useState<EventObject | null>(null);
	const [currentInputItems, setCurrentInputItems] = useState<Option<ItemModel>>();
	const [currentOutputItems, setCurrentOutputItems] = useState<Option<ItemModel>>();
	const [currentRulesItems, setCurrentRulesItems] = useState<Option<ItemModel>>();
	const inputController = new ConnectorInputController(dispatch);
	const outputController = new ConnectorOutputController(dispatch);
	const rulesController = new RulesController(dispatch);
	const userValue = useAppSelector((store) => store.UserReducer?.user);
	const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1);
	const event = useAppSelector(store => store.DHTMLXReducer.event);

	const CustomModalStylesConfig: ChakraProps = {
		p: "24px 16px",
		fontFamily: "Dosis",
		fontSize: "16px",
		width: "auto",
		minW: "900px",
		overflowY: "auto",
		whiteSpace: "pre",
		...style,
	};

	const fetchData = async (controller: any, config: any, setItems: React.Dispatch<any>) => {
		try {
			const object = await controller(config?.params, null);
			setItems(object);
		} catch (error) {
			console.error("Error fetching data:", error);
		}
	};

	useEffect(() => {
		if (userValue) {
			if (userValue?.id === undefined) return;
			const reqConfig = {
				params: {
					page:
						currentPaginatorPosition === 1
							? 0
							: currentPaginatorPosition > 0
								? currentPaginatorPosition - 1
								: 0,
					size: 100,
					userId: parseInt(userValue.id, 10),
				},
			};
			inputController.getAll(reqConfig?.params, null).then((object) => {
				setCurrentInputItems(object)
			});
			outputController.getAll(reqConfig?.params, null).then((object) => {
				setCurrentOutputItems(object)
			});
			rulesController.getAll(reqConfig?.params, null).then((object) => {
				setCurrentRulesItems(object)
			});
		}
	}, [userValue, currentPaginatorPosition]);

	const [selectedItems, setSelectedItems] = useState<any[]>([]);

	const handleOnclickNew = (elem: any, remove: Boolean) => {
		setSelectedItems(prevSelectedItems => {
			const isSelected = prevSelectedItems.some(item => item.versionId === elem.versionId);
			if (isSelected) {
				return remove ? prevSelectedItems.filter(item => item.versionId !== elem.versionId) : prevSelectedItems;
			} else {
				return [...prevSelectedItems, elem];
			}
		});
	};

	useEffect(() => {
		setLastEvent(null)
		if (event !== null && event.actions === ActionsType.ADDED) {
			console.log("info", event)
			if (event.componentType !== ComponentType.Input && event.componentType !== ComponentType.Rules && event.componentType !== ComponentType.Output) {
				return;
			}
			setSelectedItems([]);
			setClose(false);
			setLastEvent(event)
			let items: ItemModel[];
			switch (event.componentType) {
				case ComponentType.Input:
					items = Array.isArray(currentInputItems?.Some) ? currentInputItems?.Some! : [];
					break;
				case ComponentType.Rules:
					items = Array.isArray(currentRulesItems?.Some) ? currentRulesItems?.Some! : [];
					break;
				case ComponentType.Output:
					items = Array.isArray(currentOutputItems?.Some) ? currentOutputItems?.Some! : [];
					break;
				default:
					items = [];
					break;
			}
			setList(
				items.map((elem: ItemModel, idx: number) => {
					const ItemConfig: Array<ListItemConfig | false> = [
						{
							content: <Text>{elem.versionId}</Text>,
							isLink: false,
						},
						{
							content: <Text>{elem.name}</Text>,
							isLink: false,
						},
						{
							content: <Text>{elem.type}</Text>,
							isLink: false,
						},
						event?.componentType === ComponentType.Rules && {
							content: <Text>{elem.ruleType}</Text>,
							isLink: false,
						}
					].filter(Boolean);
					return (
						<ListItemRow
							key={idx}
							list={ItemConfig.filter(Boolean) as ListItemConfig[]}
							clickAction={() => handleOnclickNew(elem, false)}
							style={{ fontSize: "35px" }}
						/>
					);
				})
			)
		}
	}, [event]);

	const handleOnclick = (selectedItems: any[]) => {
		if (lastEvent !== null) {
			dispatch({
				type: DHTMLXTypes.EVENT_TOOLTIP,
				payload: {
					componentType: ComponentType.Models,
					id: lastEvent?.id,
					actions: ActionsType.SET,
					object: selectedItems
				}
			});
		}
		setClose(true);
	};

	return (
		<Modal closeOnOverlayClick={false} isOpen={!close} onClose={() => setClose(true)} isCentered={true}>
			<ModalOverlay />
			<ModalContent {...CustomModalStylesConfig}>
				<ModalCloseButton />
				<ModalBody py="32px">
					<Box mb="4" display="flex" flexWrap="wrap">
						{selectedItems.map((item) => (
							<Button
								key={item.versionId}
								mr="2"
								colorScheme="teal"
								variant="solid"
								size="sm"
								mb={"2"}
								onClick={() => handleOnclickNew(item, true)}
							>
								{item.versionId} ✕
							</Button>
						))}

					</Box>
					{list}
					<Flex>
						<MainButton ml="auto" mt={"10px"}
							onClick={() => handleOnclick(selectedItems)}>Выбрать</MainButton>
					</Flex>
				</ModalBody>
				<ModalFooter pos="relative" />
			</ModalContent>
		</Modal>
	);
};
