import { Box, Flex } from "@chakra-ui/react";
import React, { FC, useEffect, useRef, useState } from "react";
import { Loader } from "../../../../components/Loader/Loader";
import { useDispatch } from "react-redux";
import { useAppSelector } from "../../../../redux/Store";
import { Grid as GridDHX } from "dhx-suite";
import { BoxStyleConfig } from "../../../Forms/FormStyleConfigs";
import { useNavigate, useParams } from "react-router";
import ToolbarGridModel from "../../../../components/DHX/ToolbarGridModel";
import { ActionsType, ComponentType } from "../../../../common/constants";
import { useFormikContext } from "formik";
import { URLPaths } from "../../../../config/application/URLPaths";
import { ConnectorInputController } from "../../../../controllers/ConnectorInputController";
import { ConnectorOutputController } from "../../../../controllers/ConnectorOutputController";
import { RulesController } from "../../../../controllers/RulesController";
import { ActionTypes } from "../../../../redux/Model/RuleReducer";

interface GridModelsProps {
	type: ComponentType;
	fromModel?: boolean;
}

const GridModels: FC<GridModelsProps> = ({ type }) => {
	const [isLoading] = useState(false);
	const dispatch = useDispatch();
	const event = useAppSelector((store) => store.DHTMLXReducer.event);
	const [selectRowId, setSelectRowId] = useState<any>(null);

	const [grid, setGrid] = useState<GridDHX>();
	const [idGrid, setIdGrid] = useState<string | null>(null);
	const gridConnectorsRef = useRef<HTMLDivElement | null>(null);

	const { id } = useParams();
	const userValue = useAppSelector((store) => store.UserReducer?.user);
	const [currentPaginatorPosition, setCurrentPaginatorPosition] = useState(1);
	const [previewPaginatorPosition, setPreviewPaginatorPosition] = useState(1);
	const [filterConfig] = useState<any>();

	const getFactoryComponent = () => {
		switch (type) {
			case ComponentType.Input:
				return {
					controller: new ConnectorInputController(dispatch),
					link: URLPaths.IN_CONNECTOR_EDIT.link,
					modelList: "modelConnectorInputList",
				};
			case ComponentType.Output:
				return {
					controller: new ConnectorOutputController(dispatch),
					link: URLPaths.OUT_CONNECTOR_EDIT.link,
					modelList: "modelConnectorOutputList",
				};
			case ComponentType.Rules:
				return {
					controller: new RulesController(dispatch),
					link: URLPaths.RULES_EDIT.link,
					modelList: "modelRuleList",
				};
			default:
				return null;
		}
	};
	const setFormikField = () => {
		if (factoryComponent?.modelList) {
			switch (type) {
				case ComponentType.Input:
					formik.setFieldValue(factoryComponent?.modelList, grid?.data.serialize().map((v, index) => ({
						componentId: v.versionId,
						launchSecondStage: false
					})))
					break;
				case ComponentType.Rules:
					formik.setFieldValue(factoryComponent?.modelList, grid?.data.serialize().map((v, index) => ({
						componentId: v.versionId,
						queueNumber: index
					})))
					break;
				case ComponentType.Output:
					formik.setFieldValue(factoryComponent?.modelList, grid?.data.serialize().map((v, index) => ({
						componentId: v.versionId
					})))
					break;
				default:
					break;
			}
		}
	}
	const factoryComponent = getFactoryComponent();
	const formik = useFormikContext();
	const navigate = useNavigate();

	const fromModel = true

	useEffect(() => {
		if (userValue && grid) {
			if (userValue?.id === undefined) return;
			const reqConfig = {
				params: {
					page: currentPaginatorPosition === previewPaginatorPosition ? 0 : currentPaginatorPosition > 0 ? currentPaginatorPosition - 1 : 0,
					size: 100,
					userId: parseInt(userValue.id, 10),
				},
			};

			if (id && factoryComponent) {
				const { controller, modelList, link } = factoryComponent;
				controller.getAll(reqConfig?.params, { modelId: id })
					.then((res) => {
						grid?.data.parse(res.Some);
						setFormikField()
						dispatch({
							type: ActionTypes.RULE_FROM_MODEL,
							payload: true
						})
					});
			}
		}
	}, [filterConfig, userValue, currentPaginatorPosition, id, grid, type]);

	useEffect(() => {
		if (event !== null && event.id === idGrid && event.componentType === ComponentType.Models) {
			switch (event.actions) {
				case ActionsType.SET:
					let currentIds = grid?.data.serialize().map((v) => v.versionId);
					if (Array.isArray(event.object)) {
						event.object.map((value) => {
							if (!currentIds?.includes(value.versionId)) {
								grid?.data.add(value);
							}
						})
					}
					setFormikField()
					break;
				default:
					break;
			}
		}
		if (event !== null && event.id === idGrid && event.componentType === type) {
			switch (event.actions) {
				case ActionsType.REMOVE:
					if (selectRowId !== null) {
						grid?.data.remove(selectRowId);
					}
					setFormikField()
					break;
				default:
					break;
			}
		}
	}, [event, grid, type]);


	useEffect(() => {
		if (gridConnectorsRef.current && !grid && factoryComponent) {
			const newGrid = new GridDHX(gridConnectorsRef.current, {
				columns: [
					{ width: 50, id: "versionId", header: [{ text: "ID" }] },
					{
						minWidth: 100,
						// maxWidth: 450,
						id: "name",
						header: [{ text: "Name" }],
						mark: () => ("edit-button")
					},
					{
						hidden: type !== ComponentType.Input,
						width: type !== ComponentType.Input ? 0 : 120,
						id: "launchSecondStage",
						header: [{ text: "Запуск 2 этапа" }]
					},
					{
						hidden: type !== ComponentType.Rules,
						width: type === ComponentType.Rules ? 100 : 0,
						id: "ruleType",
						header: [{ text: "Тип результата" }]
					},
					{
						hidden: type !== ComponentType.Rules,
						width: type === ComponentType.Rules ? 110 : 0,
						id: "queueNumber",
						header: [{ text: "Очередность" }]
					},
					{ width: 90, id: "count", header: [{ text: "Счетчик" }] },
					{ width: 90, id: "error", header: [{ text: "Ошибки" }] },
					{ width: 100, id: "state", header: [{ text: "Состояние" }] },
					{ width: 160, id: "time", header: [{ text: "Время посл операции" }] }
				],
				editable: true,
				autoWidth: true,
				selection: "row",
				rowHeight: 30
			});

			newGrid?.events.on("BeforeSelect", (row: any, col: any) => {
				setSelectRowId(row.id)
			});
			newGrid?.events.on("cellClick", (row: any, col: any) => {
				if (col.id === "name") {
					if (factoryComponent.link === "/rules/edit")
						navigate(`${factoryComponent.link}/${row.versionId}/${String(fromModel)}`)
					else
						navigate(`${factoryComponent.link}/${row.versionId}`)
				}
			});

			setGrid(newGrid);
			setIdGrid("grid_" + Math.floor(Math.random() * 10000));
		}
	}, [grid]);

	return (
		<Flex flexDir="column" width={"100%"}>
			{isLoading ? (
				<Loader />
			) : (
				<Box {...BoxStyleConfig} width="80%" ml={"2%"}>
					<Box><ToolbarGridModel idEvent={idGrid} item={type} /></Box>
					<Box style={{ minHeight: "200px" }} ref={gridConnectorsRef} />
				</Box>
			)}
		</Flex>
	);
};

export default GridModels;
